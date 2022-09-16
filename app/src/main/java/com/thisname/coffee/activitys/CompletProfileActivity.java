package com.thisname.coffee.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thisname.coffee.R;
import com.thisname.coffee.activitys.models.User;
import com.thisname.coffee.activitys.providers.AuthProvider;
import com.thisname.coffee.activitys.providers.UserProvider;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CompletProfileActivity extends AppCompatActivity {


    TextInputEditText mTextInputUserName;
    Button mBtnRegister;
    AuthProvider mAuthProvider;
    UserProvider userProvider;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complet_profile);

        //Instanciar Objetos

        mTextInputUserName = findViewById(R.id.textInputUserNameRegister);
        mBtnRegister = findViewById(R.id.btnConfirm);

        alertDialog = new SpotsDialog(this,"Loading...");

        //base de datos
        mAuthProvider = new AuthProvider();
        userProvider = new UserProvider();

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    public void register(){
        String userName = mTextInputUserName.getText().toString();

        if (!userName.isEmpty()){

            updateUser(userName);

        }else {
            Toast.makeText(this, "Para Continuar rellena todos los campos", Toast.LENGTH_LONG).show();
        }
    }


    public void updateUser(final String userName){
        String id = mAuthProvider.getUid();
        User user = new User();
        user.setId(id);
        alertDialog.show();
        user.setUsername(userName);
        userProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                alertDialog.dismiss();
                if (task.isSuccessful()){
                    Intent intent = new Intent(CompletProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(CompletProfileActivity.this, "No se pudo realizar el registro", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}