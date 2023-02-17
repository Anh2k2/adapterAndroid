package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SubActivity extends AppCompatActivity {

    private EditText etId;
    private EditText etFullname;
    private EditText etPhone;
    private ImageView ivImage;
    private Button btnOk;
    private Button btnCancel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        etId = findViewById(R.id.etId);
        etFullname = findViewById(R.id.etFullname);
        etPhone = findViewById(R.id.etPhone);
        ivImage = findViewById(R.id.ivImage);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        //lay intent tu MainActivity chuyen sang
        Intent intent = getIntent();
        //Lay bundle
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            int id = bundle.getInt("Id");
            String image = bundle.getString("Image");
            String name = bundle.getString("Name");
            String phone = bundle.getString("Phone");
            etId.setText(String.valueOf(id));
            etFullname.setText(name);
            etPhone.setText(phone);
            btnOk.setText("Edit");
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lay du lieu va gui ve cho MainActivity
                int id = Integer.parseInt(etId.getText().toString());
                String name = etFullname.getText().toString();
                String phone = etPhone.getText().toString();
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putInt("Id", id);
                b.putString("Name", name);
                b.putString("Phone", phone);
                intent.putExtras(b);
                setResult(150, intent);
                finish();
            }
        });

    }
}