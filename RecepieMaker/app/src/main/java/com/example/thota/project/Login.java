package com.example.thota.aseproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

//This is the login page where we can sign in using credentials or by using facebook and google sign in.

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener  {
    private static final int RC_SIGN_IN =9001 ;
    TextView signup1;
    EditText email,password;
    Button nrmlsignin;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions mGoogleSignInOptions;
    Button fblog;
    String gid;
    private FirebaseAuth firebaseAuth;
    CallbackManager cbm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        email=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        findViewById(R.id.sign_in_button).setOnClickListener(this);;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        nrmlsignin=(Button)findViewById(R.id.signinbtn);
        nrmlsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())//firebase authentication
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent home=new Intent(Login.this,Home.class);
                                    startActivity(home);
                                }
                            }
                        });

            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        fblog=(Button)findViewById(R.id.fblog);
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        cbm=CallbackManager.Factory.create();
        fblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile","email","user_friends"));

            }
        });
        LoginManager.getInstance().registerCallback(cbm,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(Login.this,loginResult.getAccessToken().getUserId(),Toast.LENGTH_SHORT).show();
                        Intent home=new Intent(Login.this,Home.class);
                        startActivity(home);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(Login.this,"sorry ",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(Login.this,exception.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        signup1= (TextView)findViewById(R.id.signuplink);
        signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg= new Intent(Login.this,Register.class);
                startActivity(reg);
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            cbm.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this,account.getDisplayName(),Toast.LENGTH_SHORT).show();

            gid=account.getId();
           Intent home=new Intent(Login.this,Home.class);
           startActivity(home);

            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("google", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;


        }
}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
