package com.example.api;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EmergencyTasks extends AppCompatActivity {

    Spinner list;
    EditText instructions;
    CalendarView date;
    TextView location;
    ArrayList<String> all_tools;
    Button BuTtOn1;

    String idx = "";

    FirebaseDatabase fb;
    DatabaseReference tool_ref, task_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_tasks);

        BuTtOn1 = findViewById(R.id.commitBttn);
        list = findViewById(R.id.itemList);
        instructions = findViewById(R.id.instructions);
        date = findViewById(R.id.date);
        location = findViewById(R.id.Location);

        fb = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));
        tool_ref = fb.getReference("Tool Categories");
        task_ref = fb.getReference("Task");

        all_tools = new ArrayList<>();
        ArrayAdapter<String> tool_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, all_tools);
        list.setAdapter(tool_adapter);
        HashMap<String, Tool> tools_hess  = new HashMap<>();

        HashMap<String, String > fcingthing  = new HashMap<>();

        tool_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_tools.clear();
                tools_hess.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    DatabaseReference temp = tool_ref.child(item.getKey()).child("Tools");
                    fcingthing.put(item.getKey(), item.getValue(Category.class).getInterval());
                    temp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            for (DataSnapshot tool : snapshot1.getChildren()) {
                                all_tools.add(tool.getKey());
                                tools_hess.put(tool.getKey(), tool.getValue(Tool.class));

                            }
                            tool_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                idx= list.getItemAtPosition(i).toString();

                location.setText("Location ->  " + tools_hess.get(idx).getLocation());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                location.setText("");
            }
        });


        BuTtOn1.setOnClickListener(view -> {
            SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String date_date = simpleDateFormatter.format(date.getDate()).toString();
            Tool aTool = tools_hess.get(idx);
            Task task=new Task(aTool.getName(),aTool.getLocation(),instructions.getText().toString(),date_date,fcingthing.get(aTool.getCategory()),true);
            task_ref.push().setValue(task);
            
        });


    }

}