package com.example.api;

import android.os.Bundle;
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

public class ToolCorrespondentCertain extends AppCompatActivity {

    Button addTool;
    EditText name, ID, location, toolDescription;
    Spinner category;
    ArrayList<String> categoryItems;

    String toolName, toolLocation, toolCategory, description;
    int toolID;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_correspondent);

        category = findViewById(R.id.category);
        addTool = findViewById(R.id.addTool);
        name = findViewById(R.id.toolName);
        ID = findViewById(R.id.toolID);
        location = findViewById(R.id.toolLocation);
        toolDescription = findViewById(R.id.description);
        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference("Tool Categories");
        //DatabaseReference categoryRef = ref.child(1);

        categoryItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        category.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    categoryItems.add(snapshot.getKey());
                }
                adapter.notifyDataSetChanged();

                //categoryItems.add(snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });

        addTool.setOnClickListener(view -> {
            if (!name.getText().toString().equals("") &&
                    !ID.getText().toString().equals("") &&
                    !location.getText().toString().equals("")) {
                toolName = name.getText().toString();
                toolID = Integer.parseInt(ID.getText().toString());
                toolLocation = location.getText().toString();
                toolCategory = category.getSelectedItem().toString();
                description = toolDescription.getText().toString();

                Tool tool = new Tool(toolName, toolID, toolLocation, description);

                DatabaseReference toolRef = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference("Tool Categories").child(toolCategory);

                toolRef.child(toolName).setValue(tool);
            } else {
                Toast.makeText(ToolCorrespondentCertain.this, "Fill all the required fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}