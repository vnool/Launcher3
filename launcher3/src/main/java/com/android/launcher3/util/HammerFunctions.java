package com.android.launcher3.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chengliang.chengliang.ding on 18/1/11.
 */

public class HammerFunctions {

    public static String MD5_32(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Log.e(Tag, exception2String(e));
        }
        return re_md5;
    }


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

        public AlertDialog dialog;

        public void show() {

            final EditText textObj = new EditText(context);
            textObj.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if (hintText != null) {
                textObj.setHint(hintText);
            }
            dialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    //.setIcon(android.R.drawable.ic_dialog_info)
                    .setView(textObj)
                    .setPositiveButton("确定", null)

                    .setCancelable(false)
                    .show();


            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String usrtxt = textObj.getText().toString();

                    if (usrtxt.length() > minLength) {
                        callback.back(usrtxt);

                    } else {
                        Toast.makeText(context, "输入内容太短 (至少 " + minLength + "字)", Toast.LENGTH_LONG).show();
                    }

                }
            });


        }
    }
}
