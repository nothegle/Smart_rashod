package com.example.proecticus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ActivityToAddRashod extends AppCompatActivity {
    EditText rashodToAdd;
    TextView potratil, allMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_add_rashod);
    }

    @SuppressLint("SetTextI18n")
    public void Click_AddBtn(View v)
    {
        rashodToAdd = findViewById(R.id.rashodi);
        int rashod = Integer.parseInt(rashodToAdd.getText().toString());
        potratil = findViewById(R.id.Potratil);
        int allPotratil = Integer.parseInt(potratil.getText().toString());
        allMoney = findViewById(R.id.allMoney);
        int allMon = Integer.parseInt(allMoney.getText().toString());
        int newAllPotratil = allPotratil + rashod;
        potratil.setText(Integer.toString(newAllPotratil));
        int newAllMoney = allMon - rashod;
        allMoney.setText(Integer.toString(newAllMoney));
    }
}
