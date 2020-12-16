package com.example.contactbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class FetchData extends AppCompatActivity
{
    RecyclerView recyclerView;

    ArrayList<ModelClass> dataholder;
    ModelClass modelClass;
    MyAdapter myAdapter;

    Cursor cursor_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Your Contacts");

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cursor_obj=new DBManager(this).fetchRecords();
        dataholder=new ArrayList<>();

        while (cursor_obj.moveToNext())
        {
            modelClass=new ModelClass(cursor_obj.getString(1),cursor_obj.getString(2),cursor_obj.getString(3),cursor_obj.getString(4));
            dataholder.add(modelClass);
        }

        myAdapter=new MyAdapter(dataholder);
        recyclerView.setAdapter(myAdapter);
    }

    //FirebaseDatabase & its reference
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference=firebaseDatabase.getReference("contacts");

    /* Menu Items */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //1. Menu Inflater: Inflating the items from menu.xml into mainactivity
        getMenuInflater().inflate(R.menu.menu_items,menu);
        MenuItem search_menu_Item, cloud_upload_menu_item, download_from_cloud_menu_item;

        //2. Menu Item: Fetching the item icon from the menu.xml by using it's ID
        search_menu_Item=menu.findItem(R.id.search_menu_item);
        cloud_upload_menu_item=menu.findItem(R.id.cloud_upload);
        download_from_cloud_menu_item=menu.findItem(R.id.download_from_cloud);

        SearchView searchView= (SearchView)search_menu_Item.getActionView(); //This will create a textbox when search option is clicked

        //This part of code will perform action when something is typed in the search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });

        //3. Onclick events
        //3.1 Upload process into firebase database
        cloud_upload_menu_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {

                if (dataholder.isEmpty()) //checking if dataholder is empty
                {
                    Toast.makeText(getApplicationContext(), "No record to upload", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Uploading SQLite data into firebase database
                    databaseReference.child("contact_list").setValue(dataholder);

                    Toast.makeText(getApplicationContext(), "Records are uploaded successfully", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        //3.2 Download information from firebase
        download_from_cloud_menu_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if (dataholder.isEmpty()) //checking if local record is empty or not
                {
                    downloadAllData();
                }
                else
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(recyclerView.getContext()); //Pass any view to continue
                    builder.setTitle("Local Data Detected!");
                    builder.setMessage("Data from cloud will replace local data completely. Before restoring cloud data, we recommend you to update the cloud data with your current local data first to avoid data loss.");

                    builder.setPositiveButton("Restore Cloud Data", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //Deleting data from SQLite
                            Cursor cursor=new DBManager(getApplicationContext()).deleteData();

                            //User defined Method: Downloading all data from Firebase
                            downloadAllData();

                        }
                    });

                    builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Toast.makeText(getApplicationContext(), "Restoration of cloud data was cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.show();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //Database insertion user defined method implemented. We are using this method to download information from firebase
    private void processInsert(String n, String c, String p, String e)
    {
        String result= new DBManager(this).addRecord(n,c,p,e);

        Toast.makeText(getApplicationContext(), "Download successful. Please revisit this page to refresh the list", Toast.LENGTH_LONG).show();
    }

    //downloadAllData, user defined method
    private void downloadAllData()
    {
        //Data is fetched from the Firebase and added inside the SQLite Database insert method
        databaseReference.child("contact_list").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datas: dataSnapshot.getChildren())
                {
                    //calling function
                    processInsert(
                            datas.child("name").getValue().toString(),
                            datas.child("pcontact").getValue().toString(),
                            datas.child("scontact").getValue().toString(),
                            datas.child("email").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}