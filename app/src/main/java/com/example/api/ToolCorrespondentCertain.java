package com.example.api;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ToolCorrespondentCertain extends AppCompatActivity {

    Button addTool;
    EditText toolName, toolId, toolLocation, toolDescription, taskInstruction;
    Spinner toolCategory;
    HashMap<String,Category>  categories = new HashMap<>();
    ArrayList<String> categoryNames = new ArrayList<>();


    String tool_name, tool_location, tool_category, tool_description,task_istruction;
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
        taskInstruction = findViewById(R.id.taskInstruction);
        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference();
        //DatabaseReference categoryRef = ref.child(1);


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
                    !toolId.getText().toString().equals("") &&
                    !toolLocation.getText().toString().equals("")) {
                tool_name = toolName.getText().toString();
                tool_id = Integer.parseInt(toolId.getText().toString());
                tool_location = toolLocation.getText().toString();
                tool_category = toolCategory.getSelectedItem().toString();
                tool_description = toolDescription.getText().toString();
                task_istruction = taskInstruction.getText().toString();

                Tool tool = new Tool(tool_name, tool_id, tool_location, tool_description);
                Task task = new Task(tool_name, tool_location, task_istruction,getTime(categories.get(tool_category).getInterval()),"Unassigned");

                DatabaseReference toolRef = ref.child("Tool Categories").child(tool_category).child("Tools");
                DatabaseReference taskRef = ref.child("Tasks").push();

                toolRef.child(String.valueOf(tool_id)).setValue(tool);
                taskRef.setValue(task);
            } else {
                Toast.makeText(ToolCorrespondentCertain.this, "Fill all the required fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("NewApi")
    private String getTime(String interval) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        switch (interval){

            case "Weekly": date = Date.from((LocalDate.now().plusWeeks(1)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;
            case "Monthly": date = Date.from((LocalDate.now().plusMonths(1)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;
            case "Quarter yearly": date = Date.from((LocalDate.now().plusMonths(3)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;
            case "Half yearly": date = Date.from((LocalDate.now().plusMonths(6)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;
            case "Yearly": date = Date.from((LocalDate.now().plusYears(1)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;

        }
        return formatter.format(date).toString();
    }

}