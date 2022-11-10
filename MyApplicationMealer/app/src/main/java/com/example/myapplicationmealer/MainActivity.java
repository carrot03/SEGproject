package com.example.myapplicationmealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Button loginbutton;
    private Button registerbutton;

    private EditText  editTextinputEmail, editTextpassword;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextinputEmail = (EditText) findViewById(R.id.inputemail);
        editTextpassword = (EditText) findViewById(R.id.inputpassword);

        fAuth = FirebaseAuth.getInstance();
        System.out.println(fAuth);
        loginbutton = (Button) findViewById(R.id.loginbutton); //initialize
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked(v); //sends to main page
            }
        });

        registerbutton = (Button) findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

            onRegisterButtonClicked(v);
            }
        });
    }

    public void openPageMain(){
        Intent intent = new Intent(this,PageMain.class);
        startActivity(intent);
    }
    public void openRegisterOptions(){
        Intent intent = new Intent(this,RegisterOptions.class);
        startActivity(intent);
    }
    private boolean verifyInputs(String inputemail,String inputpassword ){
        //Creating the getters for the inputs


        //Creating the error messages
        if (inputemail.isEmpty()) {
            editTextinputEmail.setError("Email is required");
            editTextinputEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()) {
            editTextinputEmail.setError("Please provide a valid email");
            editTextinputEmail.requestFocus();
            return false;
        }

        if (inputpassword.isEmpty()) {
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return false;
        }

        if (inputpassword.length() < 6) {
            editTextpassword.setError("Email is required");
            editTextpassword.requestFocus();
            return false;
        }
        return true;
    }
    private void onRegisterButtonClicked(View view) {
        String inputemail = editTextinputEmail.getText().toString().trim();
        String inputpassword = editTextpassword.getText().toString().trim();
        if(!verifyInputs(inputemail,inputpassword)){
            return;
        }

        fAuth.createUserWithEmailAndPassword(inputemail, inputpassword)
         .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete (@NonNull Task< AuthResult > task)
            {
                if (task.isSuccessful()) {   //creating the user object

                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Register has been successful", Toast.LENGTH_LONG).show();
                        openRegisterOptions();
                    } else {
                        Toast.makeText(MainActivity.this, "Register has not been successful. Try again!", Toast.LENGTH_LONG).show();
                    }



                }

                else
                {
                    Toast.makeText(MainActivity.this, "Register has not been successful. Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void onLoginButtonClicked(View view) {
        String inputemail = editTextinputEmail.getText().toString().trim();
        String inputpassword = editTextpassword.getText().toString().trim();
        if(!verifyInputs(inputemail,inputpassword)){
            return;
        }

        fAuth.signInWithEmailAndPassword(inputemail, inputpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete (@NonNull Task< AuthResult > task)
                    {
                        if (task.isSuccessful()) {   //creating the user object
                            User user = new User( inputemail, inputpassword);
                            System.out.println("******************************************************************************");


                                            if (task.isSuccessful()) {
                                                Toast.makeText(MainActivity.this, "Login has been successful", Toast.LENGTH_LONG).show();
                                                openPageMain();
                                            } else {
                                                Toast.makeText(MainActivity.this, "Login has not been successful. Try again!", Toast.LENGTH_LONG).show();
                                            }



                        }

                        else
                        {
                            Toast.makeText(MainActivity.this, "Login has not been successful. Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}