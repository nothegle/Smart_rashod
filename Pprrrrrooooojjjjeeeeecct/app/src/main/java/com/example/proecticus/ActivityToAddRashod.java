package com.example.proecticus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
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
        
        View view = getLayoutInflater().inflate(R.layout.activity_main, null);
        TextView potratil = view.findViewById(R.id.Potratil);
        TextView allMoney = view.findViewById(R.id.allMoney);
        int rashod = Integer.parseInt(rashodToAdd.getText().toString());
        int allPotratil = Integer.parseInt(potratil.getText().toString());
        int allMon = Integer.parseInt(allMoney.getText().toString());
        int newAllPotratil = allPotratil + rashod;
        int newAllMoney = allMon - rashod;
        potratil.setText(Integer.toString(newAllPotratil));
        allMoney.setText(Integer.toString(newAllMoney));
    }
}
