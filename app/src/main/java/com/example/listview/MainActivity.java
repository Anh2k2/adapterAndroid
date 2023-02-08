package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //khai bao doi tuong luu tru danh sach cac contact

    private ArrayList<Contact> ContactList;

    private Adapter ListAdapter;

    private EditText etSearch;

    private ListView lstContact;

    private FloatingActionButton btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //thiet lap du lieu mau
        ContactList = new ArrayList<>();
        ContactList.add(new Contact(1, "img1", "Nguyễn Văn An", "56789056"));
        ContactList.add(new Contact(2, "img2", "Trần Thị Bích", "4567899"));
        ContactList.add(new Contact(3, "img3", "Mai Thu Hà", "6789956789"));
        ListAdapter = new Adapter(ContactList, this);
        etSearch = findViewById(R.id.etSearch);
        lstContact = findViewById(R.id.lstContact);
        btnAdd = findViewById(R.id.btnAdd);
        lstContact.setAdapter(ListAdapter);
    }


}