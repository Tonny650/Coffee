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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thisname.coffee.R;
import com.thisname.coffee.activitys.models.User;
import com.thisname.coffee.activitys.providers.AuthProvider;
import com.thisname.coffee.activitys.providers.UserProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mcircleImageViewBack;
    TextInputEditText mTextInputUserNameRegister;
    TextInputEditText mTextInputEmailRegister;
    TextInputEditText mTextInputPasswordRegister;
    TextInputEditText mTextInputPasswordRegisterConfirmation;
    Button mBtnRegister;
    AuthProvider authProvider;
    UserProvider userProvider;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Instanciar Objetos
        mcircleImageViewBack = findViewById(R.id.circleImageBack);
        mTextInputUserNameRegister = findViewById(R.id.textInputUserNameRegister);
        mTextInputEmailRegister = findViewById(R.id.textInputEmailRegister);
        mTextInputPasswordRegister = findViewById(R.id.textInputPasswordRegister);
        mTextInputPasswordRegisterConfirmation = findViewById(R.id.textInputPasswordRegisterConfirmation);
        mBtnRegister = findViewById(R.id.btnRegister);

        authProvider = new AuthProvider();
        userProvider = new UserProvider();

        alertDialog = new SpotsDialog(this,"Loading...");

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mcircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void register(){
        String userName = mTextInputUserNameRegister.getText().toString();
        String email = mTextInputEmailRegister.getText().toString();
        String password = mTextInputPasswordRegister.getText().toString();
        String passwordConfirmation = mTextInputPasswordRegisterConfirmation.getText().toString();

        if (!userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordConfirmation.isEmpty()){
            if (isEmailValid(email)){
                if(password.equals(passwordConfirmation)){
                    if (password.length() >= 6){
                        createUser(userName,email,password);
                    }else{
                        Toast.makeText(this, "La contraseña deve tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this, "Las Contraseñas no coinciden", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(this, "Email Invalido", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "Para Continuar rellena todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    /**check if an email is spelled correctly*/
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void createUser(final String userName, final String email,final String password){
        alertDialog.show();
        authProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id = authProvider.getUid();

                    User user = new User();
                    user.setId(id);
                    user.setUsername(userName);
                    user.setEmail(email);

                    userProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            alertDialog.dismiss();
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this, "No se pudo realizar el registro", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }else{
                    alertDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "No se realizo el registro", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}