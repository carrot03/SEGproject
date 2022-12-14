package com.example.myapplicationmealer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CookRegisterPage extends AppCompatActivity {
    private Button donebutton;

    private Button submitchequebutton;

    private ImageView previewimage;

    int SELECT_PICTURE = 200;

    private EditText editTextname, editTextLastName, editTextinputEmail, editTextpassword, editTextcreditCard, editTextCVV, editTextExpiry;
    private EditText editAddress;
    private FirebaseAuth fAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editTextname = (EditText) findViewById(R.id.inputname);
        editTextLastName = (EditText) findViewById(R.id.inputlastname);
        editTextinputEmail = (EditText) findViewById(R.id.inputemail);
        editTextpassword = (EditText) findViewById(R.id.inputpass);
        editTextcreditCard = (EditText) findViewById(R.id.creditcard);
        editTextCVV = (EditText) findViewById(R.id.cvv);
        editTextExpiry = (EditText) findViewById(R.id.expiry);
        editAddress = (EditText) findViewById(R.id.Address);

        fAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_cook_register_page);


        //setting done button's onclick
        donebutton = (Button) findViewById(R.id.donebutton);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClicked(donebutton);
            }
        });


        //initializing the submit button and image preview
        submitchequebutton = findViewById(R.id.submitchequebutton);
        previewimage = findViewById(R.id.previewimage);

        //setting onclick for the above
        submitchequebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();

            }
        });


    }

    private void onRegisterButtonClicked(View view) {
        //Creating the getters for the inputs

        String inputname = editTextname.getText().toString().trim();
        String inputlastname = editTextLastName.getText().toString().trim();
        String inputemail = editTextinputEmail.getText().toString().trim();
        String inputpass = editTextpassword.getText().toString().trim();
        String creditcard = editTextcreditCard.getText().toString().trim();
        String cvv = editTextCVV.getText().toString().trim();
        String expiry = editTextExpiry.getText().toString().trim();

        String Address = editAddress.getText().toString().trim();

        //Creating the error messages

        if (Address.isEmpty()) {
            editAddress.setError("Address is required");
            editAddress.requestFocus();
            return;
        }

        if (inputname.isEmpty()) {
            editTextname.setError("First name is required");
            editTextname.requestFocus();
            return;
        }
        if (inputlastname.isEmpty()) {
            editTextLastName.setError("Last name is required");
            editTextLastName.requestFocus();
            return;
        }
        if (inputemail.isEmpty()) {
            editTextinputEmail.setError("Email is required");
            editTextinputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()) {
            editTextinputEmail.setError("Please provide a valid email");
            editTextinputEmail.requestFocus();
            return;
        }

        if (inputpass.isEmpty()) {
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }

        if (inputpass.length() < 6) {
            editTextpassword.setError("Email is required");
            editTextpassword.requestFocus();
            return;
        }

        if (creditcard.isEmpty()) {
            editTextcreditCard.setError("Credit card is required");
            editTextcreditCard.requestFocus();
            return;
        }
        if (cvv.isEmpty()) {
            editTextCVV.setError("cvv is required");
            editTextCVV.requestFocus();
            return;
        }
        if (cvv.length() < 3) {
            editTextCVV.setError("The number should not be less than 3 numbers");
            editTextCVV.requestFocus();
            return;
        }

        if (expiry.isEmpty()) {
            editTextExpiry.setError("The expiry date is required");
            editTextExpiry.requestFocus();
            return;
        }

        final Address address;


        fAuth.createUserWithEmailAndPassword(inputemail, inputpass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete (@NonNull Task < AuthResult > task)
                        {
                            if (task.isSuccessful()) {   //creating the user object
                                User user = new User( inputemail, inputpass);

                                FirebaseDatabase.getInstance().getReference("names")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){

                                    public void onComplete(Task<Void> task){
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CookRegisterPage.this, "User has been successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(CookRegisterPage.this, "User has not been successful. Try again!", Toast.LENGTH_LONG).show();
                                    }

                                }
                                });
                            }

                             else
                            {
                            Toast.makeText(CookRegisterPage.this, "User has not been successful. Try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }



    //this chooses the photo
    void imageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null){
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        previewimage.setImageBitmap(selectedImageBitmap);
                    }
                }
            });


    //this takes you back to login
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}