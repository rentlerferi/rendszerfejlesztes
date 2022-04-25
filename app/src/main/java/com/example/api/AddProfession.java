package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class AddProfession extends AppCompatActivity {

    EditText userID, knowledge;
    Button add, delete;

    User user;
    String id, profession;
    int iterable;
    ArrayList<String> profList, resetList;

    DatabaseReference ref1, ref2, profRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profession);

        userID = findViewById(R.id.userID);
        knowledge = findViewById(R.id.knowledge);
        add = findViewById(R.id.addProfession);
        delete = findViewById(R.id.delProfession);

        ref1 = FirebaseDatabase.getInstance("https://rendszerfejlesztes-3b7df-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        ref2 = FirebaseDatabase.getInstance("https://rendszerfejlesztes-3b7df-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = userID.getText().toString();
                profession = knowledge.getText().toString();
                ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        for(DataSnapshot snapshot : dataSnapshot1.getChildren()){
                            if(Objects.equals(snapshot.getKey(), id)){
                                user = snapshot.getValue(User.class);
                                profRef = FirebaseDatabase.getInstance("https://rendszerfejlesztes-3b7df-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(id).child("profession");
                                //profRef.addListenerForSingleValueEvent(new);
                                profRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                            profRef.child(String.valueOf(dataSnapshot2.getChildrenCount())).setValue(profession);
                                    }




                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                                    }
                                });
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = userID.getText().toString();
                profession = knowledge.getText().toString();
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //categoryItems.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (Objects.equals(snapshot.getKey(), id)) {
                                user = snapshot.getValue(User.class);
                                profList = user.getProfession();
                                profRef = FirebaseDatabase.getInstance("https://rendszerfejlesztes-3b7df-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(id).child("profession");
                                profRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(int i = 0; i < dataSnapshot.getChildrenCount() - 1; i++) {
                                            if (Objects.equals(profList.get(i), profession)) {
                                                profRef.child(String.valueOf(i)).removeValue();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                                    }
                                });
                                resetList = new ArrayList<>();
                                for(String item : profList){
                                    if(item != null){
                                        resetList.add(item);
                                    }
                                }
                                Log.d("profList2", resetList.toString());
                                //if(!resetList.equals(profList)){
                                ref2.child(id).child("profession").setValue(resetList);


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                    }
                });
            }
        });
    }
}