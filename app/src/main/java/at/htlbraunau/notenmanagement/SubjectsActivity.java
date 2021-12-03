package at.htlbraunau.notenmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SubjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        Intent intent = this.getIntent();

        String[] subjects = intent.getStringArrayExtra("Subjects");

        ListView lstSubject = SubjectsActivity.this.findViewById(R.id.lst_subjects);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,subjects);

        lstSubject.setAdapter(arrayAdapter);

        lstSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(SubjectsActivity.this,subjects[i],Toast.LENGTH_SHORT).show();
                new Thread(() -> {
                    NotenmanagementAPI.getLFs(subjects[i]);
                }).start();
            }
        });
    }
}