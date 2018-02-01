package com.android.launcher3.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by chengliang.chengliang.ding on 18/1/11.
 */

public class Dialogs {
    public static void alert(final Context c, String message) {
        alert(c, message, null, null);
    }

    public static void alert(final Context c, String message, final View.OnClickListener okcall) {
        alert(c, message, null, okcall);
    }

    public static void alert(final Context c, String message, String title, final View.OnClickListener okcall) {


        final AlertDialog askIMEIdialog = new AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(message)
                //.setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", null)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return true;
                    }
                }).setCancelable(false)

                .show();

        askIMEIdialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (okcall != null) okcall.onClick(v);
                askIMEIdialog.dismiss();
            }
        });
    }

    public static void toast(Context c, String txt) {
        Toast.makeText(c, txt, Toast.LENGTH_LONG).show();
    }


    public static class InputDialog {

        public String title;
        public String message;
        public String hintText;
        public Returns callback;
        public Context context;
        public int minLength = 0;

        public InputDialog(Context c) {
            context = c;
        }

        public static interface Returns {
            public void back(String returnVal);
        }

        public void show() {

            final EditText textObj = new EditText(context);
            textObj.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if (hintText != null) textObj.setHint(hintText);
            final AlertDialog askIMEIdialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    //.setIcon(android.R.drawable.ic_dialog_info)
                    .setView(textObj)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String usrtxt = textObj.getText().toString();

                            if (usrtxt.length() > minLength) {
                                callback.back(usrtxt);

                            } else {
                                Toast.makeText(context, "输入内容太短 (至少 " + minLength + "字)", Toast.LENGTH_LONG).show();
                            }

                        }
                    })

                   // .setCancelable(false)
                    .show();


            // askIMEIdialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(clicker);


        }
    }
}
