package com.hbv2.icelandevents.ExtraUtilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Martin on 14.3.2017.
 */

public class PopUpMsg {

    /**
     * It display Dialog msg to the user with OK Button to press on.
     * @param title Title is the tile of the dialog.
     * @param msg Msg it msg to display on the dialog.
     * @param context context is Context
     */
    public static void dialogMsg(String title, String msg, Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * It display Toast msg to the user.
     * @param msg Msg it msg to display on Toast.
     * @param context context is Context
     */
    public static void toastMsg(String msg,Context context){
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }





}
