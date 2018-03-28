package com.example.password4launcher3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminCheck();

            }
        });



        Toast.makeText(this,"isHammerRunning: "+isHammerRunning(),Toast.LENGTH_LONG).show();

    }

    public   void adminCheck( ) {
        final EditText textObj = new EditText(this);
        textObj.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        final AlertDialog askIMEIdialog = new AlertDialog.Builder(this)
                .setTitle("密码")
                .setMessage("请输入管理员密码")
                //.setIcon(android.R.drawable.ic_dialog_info)
                .setView(textObj)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String usrtxt = textObj.getText().toString();

                        if (usrtxt.equals("lszc2018")) {
                            Snackbar.make(ScrollingActivity.this.getWindow().getDecorView(),
                                    "管理员密码是: " +GetAdminPassword(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            TextView tv =(TextView) findViewById(R.id.pwd);
                            tv.setText(Html.fromHtml("今天密码是:<h1>"+GetAdminPassword()+"</h1>"));
                        }else{
                            Toast.makeText(ScrollingActivity.this, "密码错误",Toast.LENGTH_LONG).show();
                        }

                    }
                })

                // .setCancelable(false)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    static String GetAdminPassword() {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        String time = ft.format(d);
        String md5 = MD5_32(time + "hammer");
        md5 = md5.replaceAll("[a-zA-Z]", "");
        return md5.substring(0, 5);
    } public static String MD5_32(String plainText) {
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

    public static String isHammerRunning(){
        return "no";
    }
}
