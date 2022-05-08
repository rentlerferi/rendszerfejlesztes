package com.example.api;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText log_auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> uIds = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log_auth = findViewById(R.id.Login_auth);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {

            } else {

            }
        });

        database = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));
        myRef = database.getReference();
        String code = log_auth.getText().toString();

        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    uIds.add(item.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Login(View view) {
        String uId = log_auth.getText().toString().trim().toUpperCase();
        if (uIds.contains(uId)) {
            myRef.child("Users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);

                    switch (user.role) {
                        case "Admin":
                            Intent a = new Intent(LoginActivity.this, AdminMenu.class);
                            startActivity(a);
                            finish();
                            break;
                        case "Operator":
                            Intent b = new Intent(LoginActivity.this, OperatorMenu.class);
                            startActivity(b);
                            finish();
                            break;
                        case "Repairer":
                            Intent c = new Intent(LoginActivity.this, Repairer.class);
                            c.putExtra("id", uId);
                            startActivity(c);
                            finish();
                            break;
                        case "ToolCorrespondent":
                            Intent d = new Intent(LoginActivity.this, ToolCorrespondentMenu.class);
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