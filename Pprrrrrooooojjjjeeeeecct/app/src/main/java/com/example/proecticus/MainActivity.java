package com.example.proecticus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button products;
    public TextView Date, allMoney, allExpenses, productsExpenses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date = findViewById(R.id.Date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        Date.setText(time);
        allMoney = findViewById(R.id.allMoney);
        allExpenses = findViewById(R.id.allExpenses);
        products = findViewById(R.id.products);
        productsExpenses = findViewById(R.id.productsExpenses);
    }

    public void Click_Products(View v)
    {
        Intent intent = new Intent(this, ActivityToAddRashod.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        String newExpense = data.getStringExtra("rashod");
        int newExp = 0;
        if(newExpense != null)
            newExp = Integer.parseInt(newExpense);
        int allMon = Integer.parseInt(allMoney.getText().toString());
        int allExp = Integer.parseInt(allExpenses.getText().toString());
        int prodExp = Integer.parseInt(productsExpenses.getText().toString());
        int newProdExp = prodExp + newExp;
        int newAllMon = allMon - newExp;
        int newAllExp = allExp + newExp;
        productsExpenses.setText(Integer.toString(newProdExp));
        allMoney.setText(Integer.toString(newAllMon));
        allExpenses.setText(Integer.toString(newAllExp));

    }
}
