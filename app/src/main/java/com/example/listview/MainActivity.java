package com.example.listview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    //khai bao doi tuong luu tru danh sach cac contact

    private ArrayList<Contact> ContactList;

    private Adapter ListAdapter;

    private EditText etSearch;

    private ListView lstContact;

    private FloatingActionButton btnAdd;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.optionmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mnuSortName:
                //sap xep ArrayList<Contact> theo ten
                SortByName();
                break;
            case R.id.mnuSortPhone:
                //sap xep ArrayList<Contact> theo phone
                SortByPhone();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //thiet lap du lieu mau
        ContactList = new ArrayList<>();

        ContactList.add(new Contact(2, "img2", "Trần Thị Bích", "4567899"));
        ContactList.add(new Contact(3, "img3", "Mai Thu Hà", "6789956789"));
        ContactList.add(new Contact(1, "img1", "Nguyễn Văn An", "56789056"));
        ListAdapter = new Adapter(ContactList, MainActivity.this);
        etSearch = findViewById(R.id.etSearch);
        lstContact = findViewById(R.id.lstContact);
        btnAdd = findViewById(R.id.btnAdd);
        lstContact.setAdapter(ListAdapter);

    }

    private void SortByPhone() {
        Collections.sort(ContactList, new Contact.PhoneOrder());
        ListAdapter.notifyDataSetChanged();
    }

    private void SortByName() {
        Collections.sort(ContactList, new Contact.NameOrder());
        ListAdapter.notifyDataSetChanged();
    }


}