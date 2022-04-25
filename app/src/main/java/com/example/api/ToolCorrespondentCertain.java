package com.example.api;

import static com.example.api.Helpers.getTime;

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
import java.util.HashMap;

public class ToolCorrespondentCertain extends AppCompatActivity {

    Button addTool;
    EditText toolName, toolLocation, toolDescription, taskInstruction;
    Spinner toolCategory;
    HashMap<String,Category>  categories = new HashMap<>();
    ArrayList<String> categoryNames = new ArrayList<>();


    String tool_name, tool_location, tool_category, tool_description, task_instruction;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_correspondent_certain);

        toolCategory = findViewById(R.id.toolCategory);
        addTool = findViewById(R.id.addTool);
        toolName = findViewById(R.id.toolName);
        toolLocation = findViewById(R.id.toolLocation);
        toolDescription = findViewById(R.id.toolDescription);
        taskInstruction = findViewById(R.id.taskInstruction);
        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        toolCategory.setAdapter(adapter);

        ref.child("Tool Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                categoryNames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    categories.put(snapshot.getKey(),category);
                    categoryNames.add(snapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });

        addTool.setOnClickListener(view -> {
            if (!toolName.getText().toString().equals("") &&
                    !toolLocation.getText().toString().equals("")) {
                tool_name = toolName.getText().toString();
                tool_location = toolLocation.getText().toString();
                tool_category = toolCategory.getSelectedItem().toString();
                tool_description = toolDescription.getText().toString();
                task_instruction = taskInstruction.getText().toString();

                Tool tool = new Tool(tool_name, tool_location, tool_description);
                Task task = new Task(tool_name, tool_location, task_instruction, getTime(categories.get(tool_category).getInterval()), "Unassigned", categories.get(tool_category).getInterval());

                DatabaseReference toolRef = ref.child("Tool Categories").child(tool_category).child("Tools");
                DatabaseReference taskRef = ref.child("Tasks").child(tool_name);

                toolRef.child(tool_name).setValue(tool);
                taskRef.setValue(task);
            } else {
                Toast.makeText(ToolCorrespondentCertain.this, "Fill all the required fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}