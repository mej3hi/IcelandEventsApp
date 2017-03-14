package com.hbv2.icelandevents.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hbv2.icelandevents.Entities.Event;
import com.hbv2.icelandevents.Entities.UserInfo;
import com.hbv2.icelandevents.ExtraUtilities.ConverterTools;
import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestEvent;
import com.hbv2.icelandevents.HttpResponse.HttpResponseMsg;
import com.hbv2.icelandevents.R;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;


public class CreateEventActivity extends AppCompatActivity implements Validator.ValidationListener{
    private Event event;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 3, maxLength = 32, message = "Please use between 3 and 32 characters.")
    private EditText textName;

    @Required(order = 3)
    @TextRule(order = 4, minLength = 3, maxLength = 32, message = "Please use between 3 and 32 characters.")
    private EditText textLocation;

    @Required(order = 6)
    @TextRule(order = 7, minLength = 3, maxLength = 250, message = "Please use between 3 and 250 characters.")
    private EditText textDescription;

    @Required(order = 8)
    @Regex( order = 9, pattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]", message = "Please use hh:mm with 24 format.")
    private EditText textTime;

    @Required(order = 10)
    private EditText textDate;

    @Required(order = 11)
    private TextView textImageUrl;

    private Validator validator;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textName = (EditText) findViewById(R.id.eventNameText);
        textLocation = (EditText) findViewById(R.id.locationText);
        textDescription = (EditText) findViewById(R.id.descriptionText);
        textTime = (EditText) findViewById(R.id.timeText);
        textDate = (EditText) findViewById(R.id.dateText);
        textImageUrl = (TextView) findViewById(R.id.imageUrlText);

        validator = new Validator(this);
        validator.setValidationListener(this);

        TextView signInAs = (TextView) findViewById(R.id.signInAsIdTextView);
        signInAs.setText("Signed in as : "+ UserInfo.getUsername());

        event = new Event();

        setDateField();
        setTimeField();
    }

    private void setDateField(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                textDate.setText(ConverterTools.toDateFormat(calendar));
            }
        };

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateEventActivity.this,date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setTimeField(){
        final TimePickerDialog.OnTimeSetListener time =  new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                textTime.setText(ConverterTools.toTimeFormat(calendar));
            }
        };

        textTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateEventActivity.this,time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
            }
        });
    }

    public void upImageBtnOnclick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            android.net.Uri selectedImage = imageReturnedIntent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            event.setImageurl(filePath);
            textImageUrl.setText(filePath.replaceAll(".+/(.*)$","$1"));
        }
    }


    @Override
    public void onValidationSucceeded() {
        String name = textName.getText().toString();
        event.setName(name);

        String location = textLocation.getText().toString();
        event.setLocation(location);

        String description = textDescription.getText().toString();
        event.setDescription(description);

        String time = textTime.getText().toString();
        event.setTime(time);

        String date = textDate.getText().toString();
        event.setDate(date);

        event.setMusicgenres(getRadioBtnValue());

        new HttpRequestEvent().createEventPost(event, event.getImageurl());

    }

    public void onValidationFailed(View view, Rule<?> rule) {
        final String failureMessage = rule.getFailureMessage();
        if (view instanceof EditText) {
            EditText failed = (EditText) view;
            failed.requestFocus();
            failed.setError(failureMessage);
        } else {
            Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public void createBtnOnClick(View view) {
        validator.validate();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioBtnOther:
                if (checked)
                    event.setMusicgenres("Other");
                    break;
            case R.id.radioBtnRock:
                if (checked)
                    event.setMusicgenres("Rock");
                    break;
            case R.id.radioBtnPop:
                if (checked)
                    event.setMusicgenres("Pop");
                    break;
            case R.id.radioBtnJazz:
                if (checked)
                    event.setMusicgenres("Jazz");
                    break;
        }
    }

    private String getRadioBtnValue(){
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton selected = (RadioButton)findViewById(group.getCheckedRadioButtonId());
        return selected.getText().toString();
    }

    @Subscribe
    public void onEditEvent(HttpResponseMsg response){
        if(response.getCode() == 200 && !response.getMsg().matches("(?i).*error.*")){
            PopUpMsg.toastMsg(response.getMsg(),this);
            finish();
        }else if(response.getCode() == 401) {
            PopUpMsg.toastMsg("Your session has expired, please sign in", this);
            redirectToSignIn();
        }else
            PopUpMsg.toastMsg("Something went wrong, please try again", this);
    }

    private void redirectToSignIn(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

}
