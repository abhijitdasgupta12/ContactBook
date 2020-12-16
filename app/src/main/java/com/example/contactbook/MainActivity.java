package com.example.contactbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity
{
    FloatingActionButton floatingActionButton;
    TextInputLayout nameEditText,pcontactEditText,scontactEditText,emailEditText;
    Button insertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        nameEditText=findViewById(R.id.nameEditText);
        pcontactEditText=findViewById(R.id.contactEditText);
        scontactEditText=findViewById(R.id.contactEditText2);
        emailEditText=findViewById(R.id.emailEditText);
        floatingActionButton=findViewById(R.id.fbtn);
        insertButton=findViewById(R.id.sbmt_add);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInsert(
                    nameEditText.getEditText().getText().toString(),
                    pcontactEditText.getEditText().getText().toString(),
                    scontactEditText.getEditText().getText().toString(),
                    emailEditText.getEditText().getText().toString()
                );
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FetchData.class));
            }
        });

    }

    private void processInsert(String n, String c, String p, String e)
    {
        String result= new DBManager(this).addRecord(n,c,p,e);

        nameEditText.getEditText().setText("");
        pcontactEditText.getEditText().setText("");
        scontactEditText.getEditText().setText("");
        emailEditText.getEditText().setText("");

        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }
}