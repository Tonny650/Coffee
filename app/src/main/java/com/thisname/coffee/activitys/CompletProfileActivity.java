package com.thisname.coffee.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class CompletProfileActivity extends AppCompatActivity {


    TextInputEditText mTextInputUserName;
    Button mBtnRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complet_profile);

        //Instanciar Objetos

        mTextInputUserName = findViewById(R.id.textInputUserNameRegister);
        mBtnRegister = findViewById(R.id.btnConfirm);

        //base de datos
        mAuth = FirebaseAuth.getInstance(); //Metodo de Autentificacion por "Email and Password" de FireBase.
        mFirestore = FirebaseFirestore.getInstance(); //Intancia para trabajar con la base de datos.

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
        String id = mAuth.getCurrentUser().getUid();
        Map<String,Object> map = new HashMap<>();
        map.put("username",userName);

        mFirestore.collection("Users").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
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