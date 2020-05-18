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

        //View view = getLayoutInflater().inflate(R.layout.activity_main, null);
        //TextView potratil = view.findViewById(R.id.Potratil);
        //TextView allMoney = view.findViewById(R.id.allMoney);

        //int allPotratil = Integer.parseInt(potratil.getText().toString());
        //int allMon = Integer.parseInt(allMoney.getText().toString());
        //int newAllPotratil = allPotratil + rashod;
        //int newAllMoney = allMon - rashod;
       // potratil.setText(Integer.toString(newAllPotratil));
       // allMoney.setText(Integer.toString(newAllMoney));
    }
}
