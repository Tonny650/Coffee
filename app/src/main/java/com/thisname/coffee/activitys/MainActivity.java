package com.thisname.coffee.activitys;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.thisname.coffee.R;
import com.thisname.coffee.activitys.models.User;
import com.thisname.coffee.activitys.providers.AuthProvider;
import com.thisname.coffee.activitys.providers.UserProvider;

import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity {

    TextView mtextRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;
    SignInButton mSignInButton;
    AuthProvider mAuthProvider;
    UserProvider userProvider;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RS_SING_IN = 1;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Instanciar objetos*/
        mtextRegister = findViewById(R.id.textRegister);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mButtonLogin = findViewById(R.id.btnLogin);
        mSignInButton = findViewById(R.id.btnLoginGoogle);
        userProvider = new UserProvider();
        mAuthProvider = new AuthProvider();

        alertDialog = new SpotsDialog(this,"Loading...");



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });


        //accion onClick of textRegister
        mtextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);//Inicia el activity
            }
        });
    }
    //validar usuario
    private void login(){
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        alertDialog.show();
        mAuthProvider.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                alertDialog.dismiss();
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this,"El email o la contraseña estan incorrectas",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void signInGoogle(){
        Intent signInintent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInintent, RS_SING_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RS_SING_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                }catch (ApiException e){
                    Log.w("Error","Google Sing in failed",e);
                }
                break;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        alertDialog.show();
        mAuthProvider.googleLogin(acct).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String id = mAuthProvider.getUid();
                            checkUserExist(id);

                        }else {
                            alertDialog.dismiss();
                            Log.w("Error","error", task.getException());
                        }
                    }
                });

    }

    private void checkUserExist(final String id) {
        userProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    alertDialog.dismiss();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);

                }else {
                    String email = mAuthProvider.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    userProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            alertDialog.dismiss();
                            if (task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this, CompletProfileActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this,"Error al almacenar la informacion en la base de datos",Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });
    }

    private class Builder {
    }
}