package com.example.api;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ToolCorrespondentCategory extends AppCompatActivity {

    ArrayList<String> intervals;
    Spinner intervalDropdown;

    EditText name, instruction;
    Button addCategory, deleteCategory;

    String categoryName, cInstructions, cInterval;
    int cNorma;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_correspondent_category);

        intervalDropdown = findViewById(R.id.intervalDropdown);
        name = findViewById(R.id.categoryName);
        instruction = findViewById(R.id.instructions);
        addCategory = findViewById(R.id.addCategory);
        deleteCategory = findViewById(R.id.deleteCategory);

        intervals = new ArrayList<>();
        intervals.add("Weekly");
        intervals.add("Monthly");
        intervals.add("Quarter yearly");
        intervals.add("Half yearly");
        intervals.add("Yearly");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, intervals);
        intervalDropdown.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference("Tool Categories");

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(charSequence.toString())) {
                            for (DataSnapshot s : snapshot.getChildren()){
                                if (s.getKey().equals(charSequence.toString())){
                                    Category category = s.getValue(Category.class);
                                    instruction.setText(category.instructions);
                                    intervalDropdown.setSelection(intervals.indexOf(category.interval));
                                    break;
                                }

                            }
                        }
                        else {
                            resetValues();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addCategory.setOnClickListener(view -> {
            if (!name.getText().toString().equals("") &&
                    !instruction.getText().toString().equals("")) {

                categoryName = name.getText().toString();
                cInstructions = instruction.getText().toString();
                cInterval = intervalDropdown.getSelectedItem().toString();

                Category category = new Category(cInterval, cInstructions);
                ref.child(categoryName).setValue(category);

                resetValues();
                name.setText("");
            } else {
                Toast.makeText(ToolCorrespondentCategory.this, "Fill all the required fields!", Toast.LENGTH_SHORT).show();
            }
        });


        deleteCategory.setOnClickListener(view -> ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(snapshot.getKey(), name.getText().toString())) {
                        if (!name.getText().toString().equals("")) {
                            categoryName = name.getText().toString();
                            ref.child(categoryName).removeValue();
                            resetValues();
                            name.setText("");
                        } else {
                            Toast.makeText(ToolCorrespondentCategory.this, "Fill the name field!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        }));
    }

    private void resetValues(){
        instruction.setText("");
        intervalDropdown.setSelection(0);
    }
}