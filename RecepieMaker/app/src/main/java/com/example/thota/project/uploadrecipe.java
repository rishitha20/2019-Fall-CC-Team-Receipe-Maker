package com.example.thota.aseproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//This page is used for uploading new recepies by the logged in user.

public class uploadrecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<String> ingredients = new ArrayList<String>();
    Spinner ingergedients,quantity;
    String ingred;
    Button addin,list,imageupload,uplodrec;
    Bitmap bitmap = null;
    Uri selectedImage;

    FirebaseAuth firebaseAuth;
    EditText title, numofppl, time, instructions;
   public DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadrecipe);
    //    userd = FirebaseDatabase.getInstance().getReference("UserDetails");
        String id = FirebaseAuth.getInstance().getUid();
 ref=FirebaseDatabase.getInstance().getReference().child("recipes");
        //  ingredients.add("");ingredients.add("");ingredients.add("");ingredients.add("");ingredients.add("");ingredients.add("");ingredients.add("");
        ingergedients=(Spinner)findViewById(R.id.spinner);
        quantity=(Spinner)findViewById(R.id.quanti);
        addin=(Button)findViewById(R.id.addin);

        uplodrec=(Button)findViewById(R.id.addrec);
        title=(EditText)findViewById(R.id.rectitle);
        numofppl=(EditText)findViewById(R.id.serve);
        time=(EditText)findViewById(R.id.timetaken);
        instructions=(EditText)findViewById(R.id.instrct);
        //title=(EditText)findViewById(R.id.rectitle);
        uplodrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeCard recipecard= new RecipeCard(title.getText().toString(),instructions.getText().toString(),numofppl.getText().toString(),ingredients,time.getText().toString());
                //userd.setValue(recipecard);
           ref.child(title.getText().toString()).setValue(recipecard);

           Toast.makeText(uploadrecipe.this,"recipe Uploaded",Toast.LENGTH_SHORT).show();
            }
        });

        addin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(uploadrecipe.this,"item is added",Toast.LENGTH_SHORT).show();
                ingredients.add(ingred);
                System.out.println(ingredients);
                ingred="";
            }
        });
        list=(Button)findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(uploadrecipe.this);

                builderSingle.setTitle("Ingredients");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(uploadrecipe.this, android.R.layout.select_dialog_singlechoice,ingredients);



                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(uploadrecipe.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Do You Want To Remove this Item ?");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                //System.out.print(arrayAdapter.getItem(which));
                                ingredients.remove(strName);
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();
            }
        });
        ArrayAdapter<CharSequence> adapter1= ArrayAdapter.createFromResource(this,R.array.quantity,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantity.setAdapter(adapter1);
        quantity.setOnItemSelectedListener(this);
        ingergedients.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.ingredients,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingergedients.setAdapter(adapter);
        ingergedients.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      //  ingred=parent.getItemAtPosition(position).toString();

        switch(parent.getId()){
            case R.id.spinner:
                ingred=parent.getItemAtPosition(position).toString();
                System.out.println(ingred);
                break;
            case R.id.quanti:
                ingred=ingred+"-"+parent.getItemAtPosition(position).toString();
                System.out.println(ingred);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==3 && resultCode==Activity.RESULT_OK){
             selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

