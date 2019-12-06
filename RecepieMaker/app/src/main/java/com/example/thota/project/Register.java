package com.example.thota.aseproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//This is the code for the register page for sending the details of the registered user to the firebase.

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText password,lastname,firstname,email,phone,confirmpass;
    ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        pd=new ProgressDialog(this);
        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText)findViewById(R.id.lastname);
        password=(EditText)findViewById(R.id.password);
        confirmpass=(EditText)findViewById(R.id.confirmpass);
        phone=(EditText)findViewById(R.id.phone);
        signup=(Button)findViewById(R.id.signup);
        email=(EditText)findViewById(R.id.email);
        email=(EditText)findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();
        signup.setOnClickListener(this);
    }
    // User details to be filled.
    public void registerUser(){
        String firstname1=firstname.getText().toString().trim();
        String lastname1=lastname.getText().toString().trim();
        String password1=password.getText().toString().trim();
        String confirmpass1=confirmpass.getText().toString().trim();
        String email1=email.getText().toString().trim();
        if(TextUtils.isEmpty(firstname1)&&TextUtils.isEmpty(lastname1)&&TextUtils.isEmpty(email1)&&TextUtils.isEmpty(password1)&&TextUtils.isEmpty(confirmpass1)){
            Toast.makeText(this,"please fill all details",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password1.equals(confirmpass1)){
            pd.setMessage("Registering");
            pd.show();
            firebaseAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this,"registration successful",Toast.LENGTH_SHORT).show();//registration successful
                        pd.cancel();
                        Intent intent=new Intent(Register.this,Login.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Register.this,"failed to register",Toast.LENGTH_SHORT).show();//registration failed
                    }
                }
            });
//Registering process completed
        }
        else
        {
            Toast.makeText(this,"Password Miss Match",Toast.LENGTH_SHORT).show();//passwords do not Match
            return;
        }
    }
    @Override
    public void onClick(View v) {
        if(v==signup){
         registerUser();
        }
    }
}
