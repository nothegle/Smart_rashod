package com.example.proecticus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences sPref;
    final String ALLMONEY = "ALLMONEY";
    final String  ALLEXPENCSES = "ALLEXPENCSES";

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
        LoadData();
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

    private void SaveData(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(ALLMONEY, allMoney.getText().toString());
        ed.putString(ALLEXPENCSES, allExpenses.getText().toString());
        ed.commit();
    }

    private void LoadData(){
        sPref = getPreferences(MODE_PRIVATE);
        String savedAllMoney = sPref.getString(ALLMONEY, "0");
        String savedAllExpenses = sPref.getString(ALLEXPENCSES, "0");
        allMoney.setText(savedAllMoney);
        allExpenses.setText(savedAllExpenses);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveData();
    }
}
