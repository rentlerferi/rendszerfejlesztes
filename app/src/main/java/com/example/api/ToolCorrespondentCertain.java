package com.example.api;

import android.os.Bundle;
import android.util.Log;
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
    EditText toolName, toolId, toolLocation, toolDescription;
    Spinner toolCategory;
    ArrayList<String> categoryItems;

    String tool_name, tool_location, tool_category, tool_description;
    int tool_id;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_correspondent_certain);

        toolCategory = findViewById(R.id.toolCategory);
        addTool = findViewById(R.id.addTool);
        toolName = findViewById(R.id.toolName);
        toolId = findViewById(R.id.toolId);
        toolLocation = findViewById(R.id.toolLocation);
        toolDescription = findViewById(R.id.toolDescription);
        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference("Tool Categories");
        //DatabaseReference categoryRef = ref.child(1);

        categoryItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        toolCategory.setAdapter(adapter);

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
            if (!toolName.getText().toString().equals("") &&
                    !toolId.getText().toString().equals("") &&
                    !toolLocation.getText().toString().equals("")) {
                tool_name = toolName.getText().toString();
                tool_id = Integer.parseInt(toolId.getText().toString());
                tool_location = toolLocation.getText().toString();
                tool_category = toolCategory.getSelectedItem().toString();
                tool_description = toolDescription.getText().toString();

                Tool tool = new Tool(tool_name, tool_id, tool_location, tool_description);

                DatabaseReference toolRef = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference("Tool Categories").child(tool_category);

                toolRef.child(String.valueOf(tool_name)).setValue(tool);
            } else {
                Toast.makeText(ToolCorrespondentCertain.this, "Fill all the required fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}