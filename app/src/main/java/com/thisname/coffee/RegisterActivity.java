package com.thisname.coffee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mcircleImageViewBack;
    TextInputEditText mTextInputUserNameRegister;
    TextInputEditText mTextInputEmailRegister;
    TextInputEditText mTextInputPasswordRegister;
    TextInputEditText mTextInputPasswordRegisterConfirmation;
    Button mBtnRegister;

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
                Toast.makeText(this, "Email valido", Toast.LENGTH_LONG).show();
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
}