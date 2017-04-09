package com.hbv2.icelandevents.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.hbv2.icelandevents.Service.NetworkChecker;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity implements Validator.ValidationListener{
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
    private EditText textTime;

    @Required(order = 9)
    private EditText textDate;

    private TextView textImageUrl;

    private Validator validator;
    private Calendar calendar;
    private ProgressBar loadingDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textName = (EditText) findViewById(R.id.nameText);
        textLocation = (EditText) findViewById(R.id.locationText);
        textDescription = (EditText) findViewById(R.id.descriptionText);
        textTime = (EditText) findViewById(R.id.timeText);
        textDate = (EditText) findViewById(R.id.dateText);
        textImageUrl = (TextView) findViewById(R.id.imageUrlText);

        validator = new Validator(this);
        validator.setValidationListener(this);

        calendar = Calendar.getInstance();

        loadingDisplay = (ProgressBar) findViewById(R.id.LoadingDisplayEE);
        loadingDisplay.setVisibility(View.INVISIBLE);

        TextView signInAs = (TextView) findViewById(R.id.signInAsIdTextView);
        signInAs.setText("Signed in as : "+ UserInfo.getUsername());

        getEvent();
        setFields();
        setDateField();
        setTimeField();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Setting Click Listener for the textDate(EditText)
     * to show DatePickerDialog when it's clicked
     */
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
               DatePickerDialog dpd = new DatePickerDialog(EditEventActivity.this,date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(ConverterTools.toDay());
                dpd.show();
            }
        });
    }

    /**
     * Setting Click Listener for the textTime(EditText)
     * to show TimePickerDialog when it's clicked
     */
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
                new TimePickerDialog(EditEventActivity.this,time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
            }
        });
    }

    /**
     * Gets the event which user clicked in MyEventActivity
     */
    private void getEvent(){
        String parsed = getIntent().getStringExtra("EVENT_NAME");
        event = ConverterTools.toEvent(parsed);
    }

    /**
     * Sets the text fields with the values from the retrieved event
     */
    private void setFields(){
        textName.setText(event.getName());
        textLocation.setText(event.getLocation());
        textDescription.setText(event.getDescription());
        textTime.setText(event.getTime());
        textDate.setText(ConverterTools.toDateFormat(event.getDate()));
        textImageUrl.setText(event.getImageurl().replaceAll(".*/(.*)$","$1"));
        radioButtonSet(event.getMusicgenres());
    }

    /**
     * User selects an Image from a Chooser
     * @param view is the GUI Component
     */
    public void upImageBtnOnclick(View view) {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermission();
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
    }

    /**
     * Retrieving the image Uri from the image which user selected and its filepath
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            android.net.Uri selectedImage = imageReturnedIntent.getData();
            String[] column = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, column, null, null, null);

            String filePath = getFilepath(cursor, column);

            if(filePath == null){
                String wholeID = DocumentsContract.getDocumentId(selectedImage);
                String id = wholeID.split(":")[1];
                String sel = MediaStore.Images.Media._ID + "=?";
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{ id }, null);

                filePath = getFilepath(cursor,column);
            }

            if(filePath == null) {
                PopUpMsg.dialogMsg("Image Failure",
                        "Something went wrong, try uploading image from another resource"
                        , EditEventActivity.this);
                return;
            }

            event.setImageurl(filePath);
            textImageUrl.setText(filePath.replaceAll(".*/(.*)$","$1"));
        }
    }

    /**
     * Click listener for Save Button when clicked
     * calls validator to validate the event form.
     * @param view
     */
    public void saveBtnOnClick(View view) {
        validator.validate();
    }

    /**
     * Validation for the Event form was successful,
     * therefore setting all the parameters to the event
     * entity and call the saveEvent() method
     */
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

        saveEvent();
    }

    /**
     * Validation did not succeed, thereby the first error message is shown
     * @param view View is the GUI component
     * @param rule Rule contains the error message
     */
    @Override
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

    /**
     * Sends HttpRequest containing the event entity
     * requesting the event to be updated/saved
     */
    private void saveEvent(){
        if(NetworkChecker.isOnline(this)) {
            loadingDisplay.setVisibility(View.VISIBLE);
            new HttpRequestEvent().editEventPost(event, event.getImageurl());
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    /**
     * Pops up an Alert Dialog making sure
     * whether user wants to remove the event
     */
    public void removeBtnOnClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you really want to remove this event?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        removeEvent();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * Sends HttpRequest containing the event id
     * requesting the event to be removed
     */
    private void removeEvent(){
        if(NetworkChecker.isOnline(this)) {
            loadingDisplay.setVisibility(View.VISIBLE);
            new HttpRequestEvent().removeEventGet(event.getId());
        }else{
            PopUpMsg.toastMsg("Network isn't available",this);
        }
    }

    /**
     * Receiving Respond from the backend server.
     * @param response Response has the Code and the Message from backend server
     */
    @Subscribe
    public void onEditEvent(HttpResponseMsg response){
        loadingDisplay.setVisibility(View.INVISIBLE);
        if(response.getCode() == 200 && !response.getMsg().matches("(?i).*error.*")){
            PopUpMsg.toastMsg(response.getMsg(),this);
            finish();
        }else if(response.getCode() == 401) {
            PopUpMsg.toastMsg("Your session has expired, please sign in", this);
            redirectToSignIn();
        }else
            PopUpMsg.toastMsg("Something went wrong, please try again", this);
    }

    /**
     * Selects the relevant radio button from the retrieved event entity
     */
    private void radioButtonSet(String genre){
        switch(genre) {
            case "Other":
                ((RadioButton) findViewById(R.id.radioBtnOther)).setChecked(true);
                break;
            case "Rock":
                ((RadioButton) findViewById(R.id.radioBtnRock)).setChecked(true);
                break;
            case "Pop":
                ((RadioButton) findViewById(R.id.radioBtnPop)).setChecked(true);
                break;
            case "Jazz":
                ((RadioButton) findViewById(R.id.radioBtnJazz)).setChecked(true);
                break;
        }
    }

    /**
     * @return text value (other,pop,rock,jazz)
     *         from the selected radio button
     */
    private String getRadioBtnValue(){
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton selected = (RadioButton)findViewById(group.getCheckedRadioButtonId());
        return selected.getText().toString();
    }

    /**
     * Redirects user to SignInActivity
     */
    private void redirectToSignIn(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * Help function for the method: onActivityResult
     * @return the filepath from the image which the user selected
     */
    private String getFilepath(Cursor cursor, String[] column){
        if (cursor == null)
            return null;

        int columnIndex = cursor.getColumnIndex(column[0]);

        cursor.moveToFirst();

        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }

    /**
     * Request permission to read external storage
     * for retrieving image from library and other resources.
     * (This is necessary for API level 23 and above)
     */
    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                123);
    }

    /**
     * Handling the user respond for the requested permission (accept/decline)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    upImageBtnOnclick(textImageUrl);
                } else {
                    PopUpMsg.dialogMsg("Permisson",
                            "The App must be granted permission, to retrieve image from gallery"
                            ,this);
                }
            }

        }
    }

}
