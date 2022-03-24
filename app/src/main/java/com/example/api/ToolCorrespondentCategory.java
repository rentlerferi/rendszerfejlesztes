package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ToolCorrespondentCategory extends AppCompatActivity {

    ArrayList<String> intervals;
    Spinner intervalDropdown;

    EditText name, instruction, norma;
    Button addCategory;

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
        norma = findViewById(R.id.norma);
        addCategory = findViewById(R.id.addCategory);

        intervals = new ArrayList<>();
        intervals.add("weekly");
        intervals.add("monthly");
        intervals.add("quarter yearly");
        intervals.add("half yearly");
        intervals.add("yearly");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, intervals);
        intervalDropdown.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance("https://rendszerfejlesztes-3b7df-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Tool Categories");


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().equals("") &&
                    !instruction.getText().toString().equals("") &&
                    !norma.getText().toString().equals("")) {

                    categoryName = name.getText().toString();
                    cInstructions = instruction.getText().toString();
                    cInterval = intervalDropdown.getSelectedItem().toString();
                    cNorma = Integer.parseInt(norma.getText().toString());

                    Category category = new Category(cInterval, cInstructions, cNorma);

                    ref.child(categoryName).setValue(category);
                } else {
                    Toast.makeText(ToolCorrespondentCategory.this, "Fill all the required fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}