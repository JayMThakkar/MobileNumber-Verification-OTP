package com.example.dell.mob_number;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText phonennumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phonennumber = findViewById(R.id.editText);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phonennumber.getText().toString().trim();
                //String phone = "+India"+phonennumber;
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                i.putExtra("phone",phone);
                startActivity(i);
            }
        });
    }
}
