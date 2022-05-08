package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddProfession extends AppCompatActivity {
    EditText professionName;
    ListView professionList;
    ArrayList<String>  professions = new ArrayList<>();
    HashMap<String,String> professionMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profession);

        professionName = findViewById(R.id.professionName);
        professionList = findViewById(R.id.professionList);

        ArrayAdapter<String> professionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,professions);
        professionList.setAdapter(professionsAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference("Professions");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                professions.clear();
                professionMap.clear();
               for(DataSnapshot s : snapshot.getChildren()) {
                   professionMap.put(s.getKey(),s.getKey());
                   professions.add(s.getKey());
               }
               professionsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        professionName.setOnClickListener(t ->{
            if(!professions.contains(professionName.getText().toString())){
                professions.add(professionName.getText().toString());
                professionMap.put(professionName.getText().toString(),professionName.getText().toString());
                ref.setValue(professionMap);
            }
        });

    }
}