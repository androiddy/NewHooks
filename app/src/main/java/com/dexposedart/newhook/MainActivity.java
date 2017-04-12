package com.dexposedart.newhook;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edit_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_query = (EditText) findViewById(R.id.edit_query);
    }

    public void button(View view) {
        Log.e("123", Toasts(new Boolean[]{true}, edit_query.getText().toString(), new View[]{view}));
    }


    public void button1(View view) {
       test1((Button) view);
    }

    public void button2(View view) {
        edit_query.setText(test111());
    }

    public static String Toasts(Boolean[] booleen, String msg, View[] view) {
        Toast.makeText(App.getContext(), msg, 0).show();
        return msg;
    }

    public String test111(){
        return "测试3";
    }


    public static void test1(Button button){
        Toast.makeText(App.getContext(), button.getText(), 0).show();
    }
}
