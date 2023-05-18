package com.example.listview;


import static androidx.core.content.PermissionChecker.checkSelfPermission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
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
import android.widget.Toast;
import android.Manifest;

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
    private int SelectedItemId;
    private MyDB db;
    private ContentProvider cp;

    int PERMISSIONS_REQUEST_READ_CONTACT = 100;

    ConnectionReceiver connectionReceiver;

    IntentFilter intentFilter;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST_READ_CONTACT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Permisson is granted
                this.ShowContact();
            }else {
                Toast.makeText(this, "Until you grant the permission, ", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
            case R.id.mnuBroadcast:
                Intent intent = new Intent("com.example.listview.SOME_ACTION");
                sendBroadcast(intent);
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
        Contact c = ContactList.get(SelectedItemId);
        switch (item.getItemId()){

            case R.id.mnuEdit:
                //Tạo đối tượng Intent để gọi tới Sub
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                //2. Truyền dữ liệu sang sub bằng bundle nếu cần

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
            case R.id.mnuDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notification");
                builder.setMessage("Do you want delete?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ContactList.remove(SelectedItemId);
                        //lstContact.setAdapter(ListAdapter);
                        db.deleteContact(ContactList.get(SelectedItemId).getId());
                        resetData();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.mnuCall:
                Intent in = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + c.getPhone()));
                startActivity(in);
                break;
            case R.id.mnuChat:
                Intent intent1 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto: " + c.getPhone()));
                intent1.putExtra("sms_body", "Nội dung tin nhắn");
                startActivity(intent1);
                break;
            case R.id.mnuEmail:
                Intent intent2 = new Intent(Intent.ACTION_SENDTO);
                intent2.setData(Uri.parse("mailto:"));
                intent2.putExtra(Intent.EXTRA_EMAIL, new String[]{"email@address.com"});
                intent2.putExtra(Intent.EXTRA_SUBJECT, "Tiêu đề email");
                intent2.putExtra(Intent.EXTRA_TEXT, "Nội dung email");
                try{
                    startActivity(intent2);
                }
                catch(ActivityNotFoundException e){
                    Toast.makeText(this, "Ứng dụng Email không tồn tại trên thiết bị của bạn", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mnuAlarm:
                Intent intent3 = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                startActivity(intent3);
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
            //ContactList.add(new Contact(id, "", name, phone));
            db.addContact(newcontact);
        }
        else if(requestCode == 200 && resultCode == 150)
        {//truong hop sua
            //ContactList.set(SelectedItemId, new Contact(id, "",name, phone));
            db.updateContact(id, newcontact);
            //ContactList = db.getAllContact();
        }
        ListAdapter.notifyDataSetChanged();
        //lstContact.setAdapter(ListAdapter);
        resetData();
    }

    private void resetData(){
        db = new MyDB(MainActivity.this, "ContactDB",null,1);
        ContactList  = db.getAllContact();
        ListAdapter = new Adapter(ContactList, MainActivity.this);
        lstContact.setAdapter(ListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //thiet lap du lieu mau
//        ContactList = new ArrayList<>();
//        ContactList.add(new Contact(2, "img2", "Trần Thị Bích", "4567899"));
//        ContactList.add(new Contact(3, "img3", "Mai Thu Hà", "6789956789"));
//        ContactList.add(new Contact(1, "img1", "Nguyễn Văn An", "56789056"));

        //tao moi csdl
        db = new MyDB(this, "ContactDB", null, 1);
        //them du lieu LAN DAU vao db
        //db.addContact(new Contact(1, "img1", "Nguyen Van An", "0982358769"));
        //db.addContact(new Contact(2, "img2", "Tran Thi Bich", "0983358788"));
        //db.addContact(new Contact(3, "img3", "Mai Thu Ha", "0982385765"));
        ContactList = db.getAllContact();

        //ListAdapter = new Adapter(ContactList, this);
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

        ShowContact();
        //lstContact.setAdapter(ListAdapter);
        //Load Callogs
        //Load Browser history
        //Load va hien thi cac file hinh anh, lam chuong trinh giong gallery
        //Load va hien thi danh sach cac file nhac, video

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

        connectionReceiver = new ConnectionReceiver();
        intentFilter = new IntentFilter("com.example.listview.SOME_ACTION");
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANCE");
        registerReceiver(connectionReceiver, intentFilter);
    }



    private void SortByPhone() {
        Collections.sort(ContactList, new Contact.PhoneOrder());
        ListAdapter.notifyDataSetChanged();
    }

    private void SortByName() {
        Collections.sort(ContactList, new Contact.NameOrder());
        ListAdapter.notifyDataSetChanged();
    }

    private void ShowContact()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACT);
        } else{
            cp = new ContentProvider(this);
            ContactList = cp.getAllContact();
            ListAdapter = new Adapter(ContactList, this);
            lstContact.setAdapter(ListAdapter);
        }
    }

}