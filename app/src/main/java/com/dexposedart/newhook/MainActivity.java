package com.dexposedart.newhook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        Toasts(null,edit_query.getText().toString(), new View[]{view});
    }

    public static void Toasts(boolean[] s,String msg, View[] view) {
        Toast.makeText(App.getContext(), msg, 0).show();
    }
}
