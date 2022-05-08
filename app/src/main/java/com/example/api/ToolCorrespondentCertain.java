package com.example.api;

import static com.example.api.Helpers.getTime;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    ListView toolList;

    String tool_name, tool_location, tool_category, tool_description, task_instruction;
    HashMap<String,Tool> tools = new HashMap<>();
    ArrayList<String> toolNames = new ArrayList<>();

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
        toolList = findViewById(R.id.toolList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        toolCategory.setAdapter(adapter);
        ArrayAdapter<String> toolsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, toolNames);
        toolList.setAdapter(toolsAdapter);

        ref.child("Tool Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                categoryNames.clear();
                tools.clear();
                toolNames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    categories.put(snapshot.getKey(),category);
                    categoryNames.add(snapshot.getKey());
                    DatabaseReference toolsRef = ref.child("Tool Categories").child(snapshot.getKey()).child("Tools");
                    toolsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot toolsSnapshot) {
                            for(DataSnapshot tool : toolsSnapshot.getChildren()){
                                Tool t = tool.getValue(Tool.class);
                                tools.put(t.name,t);
                                toolNames.add(t.name);
                            }
                            toolsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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

                Tool tool = new Tool(tool_name, tool_location, tool_category, tool_description);
                Task task = new Task(tool_name, tool_location, task_instruction, getTime(categories.get(tool_category).interval), "Unassigned", categories.get(tool_category).interval);

                DatabaseReference toolRef = ref.child("Tool Categories").child(tool_category).child("Tools");
                DatabaseReference taskRef = ref.child("Tasks").child(tool_name);

                toolRef.child(tool_name).setValue(tool);
                taskRef.setValue(task);
            } else {
                Toast.makeText(ToolCorrespondentCertain.this, "Fill all the required fields!", Toast.LENGTH_SHORT).show();
            }
        });
        toolList.setOnItemClickListener((adapterView, view, i, l) -> {
            Pair<String, Tool> t = new Pair<>((String)toolList.getItemAtPosition(i), tools.get((String)toolList.getItemAtPosition(i)));

            AlertDialog.Builder builder = new AlertDialog.Builder(ToolCorrespondentCertain.this);
            builder.setTitle(t.first);
            builder.setMessage(String.format("Description: %s\nLocation: %s\n",
                    t.second.description, t.second.location));
            builder.setCancelable(false);

            builder.setPositiveButton(
                    "Ok",
                    (dialog, id) -> {
                        dialog.cancel();
                    });

            AlertDialog alert11 = builder.create();
            alert11.show();
        });
    }
}