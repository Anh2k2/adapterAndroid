package com.example.listview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    int SelectedItemId;

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_delete, menu);

    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuEdit:
                //Tạo đối tượng Intent để gọi tới Sub
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                //2. Truyền dữ liệu sang sub bằng bundle nếu cần
                Contact c = ContactList.get(SelectedItemId);
                Bundle b = new Bundle();
                b.putInt("Id", c.getId());
                b.putString("Image", c.getImages());
                b.putString("Name", c.getName());
                b.putString("Phone", c.getPhone());
                intent.putExtras(b);
                //3.Mở sub bằng cách gọi hàm
                //staractivity hoac staractivityforresult
                startActivityForResult(intent, 200);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle b = data.getExtras();
        int id = b.getInt("Id");
        String name = b.getString("Name");
        String phone = b.getString("Phone");
        Contact newcontact = new Contact(id, "Image", name, phone);
        if(requestCode == 100 &&resultCode == 150)
        {//truong hop them

        }
        else if(requestCode == 200 && resultCode == 150)
        {//truong hop sua

        }
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
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //1. Tao intent de mo subactivity
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                //2. Truyen du lieu sang subactivity bang bundle neu can
                //3. Mo subactivity bang cach goij ham
                //staractivity hoac staractivityforresult
                startActivityForResult(intent, 100);
            }
        });
        lstContact.setAdapter(ListAdapter);
        registerForContextMenu(lstContact);
        lstContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                SelectedItemId = i;
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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