package com.example.proecticus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ActivityToAddRashod extends AppCompatActivity {
    EditText rashodToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_add_rashod);
        rashodToAdd = findViewById(R.id.rashodi);
    }

    @SuppressLint("SetTextI18n")
    public void Click_AddBtn(View v)
    {
        Intent newExpense = new Intent();
        newExpense.putExtra("rashod", rashodToAdd.getText().toString());
        setResult(RESULT_OK, newExpense);
        finish();
    }
}
