package com.example.bmi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "Profile : ";
    private Spinner genderSpinner;
    private EditText userName, userEmail, userMobile, userWeight, userHeight, userAge;
    private Button saveButton, logoutButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private CircleImageView userImage;
    private static final int Gallery_Pick=1;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Initialised();
        Log.d(TAG, "In start function");

        databaseReference.keepSynced(true);
        ArrayAdapter<CharSequence> genderArray = ArrayAdapter.createFromResource(getContext(), R.array.gender_array, android.R.layout.simple_spinner_item);
        genderArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderArray);
        genderSpinner.setOnItemSelectedListener(this);
        RetrieveUserInfo();
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Save button\n") ;
                final String name, mobile, age, gender, weight, height;
                name = userName.getText().toString().trim();
                mobile = userMobile.getText().toString().trim();
                age = userAge.getText().toString().trim();
                gender = genderSpinner.getSelectedItem().toString().trim();
                weight = userWeight.getText().toString().trim();
                height = userHeight.getText().toString().trim();
                //name = changeCase(name);
                //name = toTitleCase(name);
                Log.d(TAG, "Before image");
                Log.d(TAG, "After Image called");

                progressBar.setVisibility(View.VISIBLE);
                UserData data = new UserData(name, firebaseAuth.getCurrentUser().getEmail(), mobile, age, gender, weight, height);
                Map<String, Object> updateValues = data.toMap();

                databaseReference.updateChildren(updateValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(),"Profile Updated Successfully.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                //view.finish();
                Toast.makeText(getContext(), "Signout Successful", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void Initialised()
    {
        userImage = view.findViewById(R.id.image_profile_frag_id);
        userName = view.findViewById(R.id.name_profile_frag_id);
        userEmail = view.findViewById(R.id.email_profile_frag_id);
        userMobile = view.findViewById(R.id.mobile_profile_frag_id);
        userAge = view.findViewById(R.id.age_profile_frag_id);
        userWeight = view.findViewById(R.id.weight_profile_frag_id);
        userHeight = view.findViewById(R.id.height_profile_frag_id);
        genderSpinner = view.findViewById(R.id.gender_profile_frag_id);
        saveButton = view.findViewById(R.id.saveButton_profile_frag_id);
        progressBar = view.findViewById(R.id.progressBar_profile_frag_id);
        scrollView = view.findViewById(R.id.scrollView_profile_frag_id);
        logoutButton = view.findViewById(R.id.logoutButton_profile_frag_id);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("users_data/"+firebaseAuth.getCurrentUser().getUid());
        storageReference= FirebaseStorage.getInstance().getReference().child("ProfileImages");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Pick && resultCode == RESULT_OK  && data!=null)
        {
            Uri imageUri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start((Activity) getContext());
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){

                Uri resultUri=result.getUri();
                progressBar.setVisibility(View.VISIBLE);

                final StorageReference filepath = storageReference.child(firebaseAuth.getCurrentUser().getUid() +".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getContext(),"Profile image updated",Toast.LENGTH_SHORT).show();
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    databaseReference.child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getContext(), "Image stored in database", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }

        }
    }

    private void RetrieveUserInfo()
    {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String age = dataSnapshot.child("age").getValue().toString();
                    String mobile = dataSnapshot.child("mobile").getValue().toString();
                    String gender = dataSnapshot.child("gender").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String weight = dataSnapshot.child("weight").getValue().toString();
                    String height = dataSnapshot.child("height").getValue().toString();

                    userAge.setText(age);
                    userName.setText(name);
                    userMobile.setText(mobile);
                    userEmail.setText(email);
                    userWeight.setText(weight);
                    userHeight.setText(height);

                    if(gender.equals("Male"))
                    {
                        genderSpinner.setSelection(0);
                    }
                    else if(gender.equals("Female"))
                    {
                        genderSpinner.setSelection(1);
                    }
                    else
                    {
                        genderSpinner.setSelection(2);
                    }

                    if(dataSnapshot.hasChild("image"))
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(userImage);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "No data found for this user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        scrollView.setVisibility(View.VISIBLE);
    }

    public String changeCase(String a){
        String result=a.toLowerCase();
        return result;
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    boolean isEmail(EditText text){
        CharSequence email=text.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    boolean isEmpty(EditText text){
        CharSequence string =text.getText().toString().trim();
        return (TextUtils.isEmpty(string));
    }
    boolean checkDataEntered()
    {
        if(isEmpty(userName))
        {
            userName.setError("Enter Name");
            userName.requestFocus();
            return false;
        }
        else if(isEmpty(userMobile)){
            userMobile.setError("Enter Mobile Number");
            userMobile.requestFocus();
            return false;
        }
        else if(userMobile.length()!=10) {
            userMobile.setError("Must 10 Digits");
            userMobile.requestFocus();
            return false;
        }
        else if(isEmpty(userAge)){
            userAge.setError("Enter Age");
            userAge.requestFocus();
            return false;
        }
        else if(isEmpty(userWeight))
        {
            userWeight.setError("Enter Weight");
            userWeight.requestFocus();
            return false;
        }
        else if(isEmpty(userHeight))
        {
            userHeight.setError("Enter Height");
            userHeight.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gender = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
