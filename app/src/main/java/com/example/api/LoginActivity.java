package com.example.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText log_auth;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> uIds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log_auth=findViewById(R.id.Login_auth);


        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInAnonymously:success");


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInAnonymously:failure", task.getException());

                        }
                    }
                });

        database = FirebaseDatabase.getInstance("https://rendszerfejlesztes-3b7df-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference();
        String code=log_auth.getText().toString();
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot item: snapshot.getChildren()) {
                    uIds.add(item.getKey());
                }
                    Log.d("SUS",uIds.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Login(View view) {
        String uId = log_auth.getText().toString().trim().toUpperCase();
        if(uIds.contains(uId)){
            Log.d("SUS","yes");
            myRef.child("Users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    Log.d("sus",user.toString());

                    switch(user.getRole()){
                        case "Admin":
                            Intent a = new Intent(LoginActivity.this, Admin.class);
                            startActivity(a);
                            finish();
                            break;
                        case "Operator":
                            Intent b = new Intent(LoginActivity.this, Operator.class);
                            startActivity(b);
                            finish();
                            break;
                        case "Repairer":
                            Intent c = new Intent(LoginActivity.this, Repairer.class);
                            startActivity(c);
                            finish();
                            break;
                        case "ToolCorrespondent":
                            Intent d = new Intent(LoginActivity.this, ToolCorrespondent.class);
                            startActivity(d);
                            finish();
                            break;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }



}