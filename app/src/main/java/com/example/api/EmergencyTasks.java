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
import android.widget.Toast;

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
import java.util.Calendar;
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
    String date_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_tasks);

        BuTtOn1 = findViewById(R.id.commitBttn);
        list = findViewById(R.id.itemList);
        instructions = findViewById(R.id.Instructions);
        date = findViewById(R.id.date);
        location = findViewById(R.id.Location);

        fb = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));
        tool_ref = fb.getReference("Tool Categories");
        task_ref = fb.getReference("Tasks");

        all_tools = new ArrayList<>();
        ArrayAdapter<String> tool_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, all_tools);
        list.setAdapter(tool_adapter);
        HashMap<String, Tool> tools_hess  = new HashMap<>();

        HashMap<String, String> categoryIntervals  = new HashMap<>();

        tool_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_tools.clear();
                tools_hess.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    DatabaseReference temp = tool_ref.child(item.getKey()).child("Tools");
                    categoryIntervals.put(item.getKey(), item.getValue(Category.class).interval);
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

                location.setText("Location ->  " + tools_hess.get(idx).location);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                location.setText("");
            }
        });

        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month++;
                String strMonth = (month < 10) ? String.format("0%d", month) : String.valueOf(month);
                String strDay = (day < 10) ? String.format("0%d", day) : String.valueOf(day);
                date_date = String.format("%d-%s-%s", year, strMonth, strDay);
            }
        });


        BuTtOn1.setOnClickListener(view -> {
            Tool aTool = tools_hess.get(idx);
            Task task = new Task(aTool.name, aTool.location, instructions.getText().toString(), date_date, categoryIntervals.get(aTool.category),true);
            task_ref.child(aTool.name).setValue(task);
            
        });


    }

}