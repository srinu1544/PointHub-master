package com.pointhub.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pointhub.R;
import com.pointhub.RegistrationDB;
import com.pointhub.wifidirect.WifiDirectSend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_login extends AppCompatActivity implements View.OnClickListener {

    private static final String MY_PREFS_NAME =  "my_data";
    private static final int PICK_IMAGE_REQUEST = 1;

    // Defining view objects.
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private CircleImageView circleImageView;
    Bitmap bitmap;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private Uri downloadUrl;
    DatabaseReference databaseReference;
    private String user_id;
    RegistrationDB details;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        // Initializing firebase auth object.
        firebaseAuth = FirebaseAuth.getInstance();

        // If getCurrentUser does not returns null.
        if (firebaseAuth.getCurrentUser() != null) {

            // That means user is already logged in so close this activity and open WifiDirect activity.
            finish();
            startActivity(new Intent(MainActivity_login.this, WifiDirectSend.class));
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Initializing views.
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        circleImageView = (CircleImageView) findViewById(R.id.circleView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        progressDialog = new ProgressDialog(this);

        // Attaching listener to button.
        buttonSignup.setOnClickListener(this);
    }

    private void registerUser() {

        // Getting email and password from edit texts.
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Checking if email and passwords are empty.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        // If the email and password are not empty displaying a progress dialog
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        // Creating a new user.
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // Checking if success.
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(),WifiDirectSend.class));
                            firebaseAuth = FirebaseAuth.getInstance();
                            user_id = firebaseAuth.getCurrentUser().getUid();
                            try {
                                details = new RegistrationDB(email, downloadUrl.toString(), "", "", "", "", "");
                                databaseReference.child("Users").child(user_id).setValue(details);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        } else {

                            // Display some message here.
                            Toast.makeText(MainActivity_login.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if (view == buttonSignup) {
            registerUser();

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("mailid", editTextEmail.getText().toString().trim());
            editor.putString("password", editTextPassword.getText().toString().trim());
            editor.apply();
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Log.d(TAG, String.valueOf(bitmap));
            circleImageView.setImageBitmap(bitmap);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference=storage.getReferenceFromUrl("gs://smartpoints-8ef37.appspot.com");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();

            StorageReference reference=storageReference.child("Profile Pictures/"+bytes);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();

            UploadTask uploadTask = reference.putBytes(bytes,metadata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(getApplicationContext(),"Upload Failed",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }
    }
}