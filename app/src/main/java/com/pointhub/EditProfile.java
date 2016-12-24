package com.pointhub;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pointhub.login.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chowd on 12-12-2016.
 */

public class EditProfile extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;

    ImageButton backbutton;
    EditText firstname_edit,lastname_edit,email_edit,mobilenumber_edit,dob_edit;
    CheckBox male,female;
    Button updatebutton,calendarButton;
    String firstName,lastName,emailAddress,mobileNumber,dob,profileImage,gender,profilePicture,user_id;
    RegistrationDB details;
    private FirebaseAuth firebaseAuth;
    DatabaseReference database,userDatabase;
    CircleImageView circleImageView;
    Bitmap bitmap;
    int year,month,day;
    private Uri downloadUrl;
    static final int DATE_PICKER_ID = 1111;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDatabase = firebaseDatabase.getReference();
        database=firebaseDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        if(user!=null){user_id=user.getUid();

        getUpdatedData();}

        firstname_edit=(EditText)findViewById(R.id.firstname);
        lastname_edit=(EditText)findViewById(R.id.lastname);
        email_edit=(EditText)findViewById(R.id.email);
        email_edit.setFocusableInTouchMode(false);email_edit.setClickable(false);
        mobilenumber_edit=(EditText)findViewById(R.id.mobilenumber);
        dob_edit=(EditText)findViewById(R.id.dob);
        circleImageView=(CircleImageView)findViewById(R.id.circleView);

        male=(CheckBox)findViewById(R.id.malecheck);
        female=(CheckBox)findViewById(R.id.femalecheck);
        backbutton=(ImageButton) findViewById(R.id.back_button);
        updatebutton=(Button)findViewById(R.id.update);
        calendarButton=(Button)findViewById(R.id.calendar);

        calendarButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                showDialog(DATE_PICKER_ID);
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        user=firebaseAuth.getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            emailAddress=user.getEmail();
            email_edit.setText(emailAddress);
        }
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Male";
                female.setChecked(false);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Female";
                male.setChecked(false);
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile.super.onBackPressed();
            }
        });

        if(firebaseAuth.getCurrentUser() != null) {

            updatebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateToFirebase();

                    Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();

                }
            });

        }
        else {
            startActivity(new Intent(EditProfile.this, LoginActivity.class));
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

    private void updateToFirebase() {

        if(firstname_edit.getText().toString().trim() != null){firstName=firstname_edit.getText().toString().trim();}
        if(lastname_edit.getText().toString().trim() != null){lastName=lastname_edit.getText().toString().trim();}
        if(mobilenumber_edit.getText().toString().trim() != null){mobileNumber=mobilenumber_edit.getText().toString().trim();}
        if(dob_edit.getText().toString().trim() != null){dob=dob_edit.getText().toString().trim();}
        if(downloadUrl != null){profileImage=downloadUrl.toString();}
        else {profileImage = profilePicture;}
        details=new RegistrationDB(emailAddress,profileImage,firstName,lastName,mobileNumber,dob,gender);
        database.child("Users").child(user_id).setValue(details);
    }

    private void getUpdatedData(){
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            DatabaseReference userRefer = userDatabase.child("Users").child(user_id);
            userRefer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if(map.get("firstName").toString() != null){firstName = map.get("firstName").toString();firstname_edit.setText(firstName);}
                        if(map.get("lastName").toString() != null){lastName = map.get("lastName").toString();lastname_edit.setText(lastName);}
                        if(map.get("mobileNumber").toString() != null){mobileNumber = map.get("mobileNumber").toString();mobilenumber_edit.setText(mobileNumber);}
                        if(map.get("dob").toString() != null){dob = map.get("dob").toString();dob_edit.setText(dob);}
                        if(map.get("gender").toString() != null){gender = map.get("gender").toString();
                        if (gender.equalsIgnoreCase("male")) {
                            male.setChecked(true);
                            female.setChecked(false);
                        }
                        if (gender.equalsIgnoreCase("female")) {
                            female.setChecked(true);
                            male.setChecked(false);
                        }}
                        profilePicture=map.get("profileImage").toString();
                        Picasso.with(EditProfile.this).load(profilePicture).into(circleImageView);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                    // open datepicker dialog.
                    // set date picker for current date
                    // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            @Override
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {

                year  = selectedYear;
                month = selectedMonth;
                day   = selectedDay;

                // Show selected date
                dob_edit.setText(new StringBuilder().append(day).append("/")
                        .append(month + 1).append("/").append(year)
                        .append(" "));

            }
        };
}