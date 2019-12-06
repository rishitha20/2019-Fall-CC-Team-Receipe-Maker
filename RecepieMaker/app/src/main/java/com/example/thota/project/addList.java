package com.example.thota.aseproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//This page is used for adding items into the list. The user can scan items or enter items manually.

public class addList extends AppCompatActivity {
   static List<String> items= new ArrayList<String>();
    EditText item;
    Button add1,conti,bck;
    ListView list1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        add1=(Button)findViewById(R.id.add);
        item=(EditText)findViewById(R.id.itemname);
        list1=(ListView)findViewById(R.id.list);
        conti=(Button)findViewById(R.id.reci);

        bck=(Button)findViewById(R.id.bck);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirect= new Intent (addList.this,Home.class);
                startActivity(redirect);
            }
        });
        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recipe= new Intent(addList.this,com.example.thota.aseproject.recipe.class);
                recipe.putExtra("items", (Serializable) items);
                startActivity(recipe);
            }
        });
        Intent intent=getIntent();
        String itemfromscan=intent.getStringExtra("item");
        if(itemfromscan.isEmpty()){}
        else{
            items.add(itemfromscan);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        list1.setAdapter(adapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(addList.this);
                builder.setMessage("Do you Want to delete this item").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      items.remove(position);
                      adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(item.getText().toString());
                item.setText("");
                adapter.notifyDataSetChanged();
            }
        });
    }
}
