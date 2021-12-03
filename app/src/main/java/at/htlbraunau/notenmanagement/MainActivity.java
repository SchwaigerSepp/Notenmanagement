package at.htlbraunau.notenmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NotenmanagementClient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLogin(View v) {

        EditText edtUsername = MainActivity.this.findViewById(R.id.edt_user);
        String username = edtUsername.getText().toString();

        EditText edtPassword = MainActivity.this.findViewById(R.id.edt_password);
        String password = edtPassword.getText().toString();

        new Thread()    {
            @Override
            public void run() {
                if(NotenmanagementAPI.login(username,password)) {
                    Log.i(TAG,"Success");

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Login Success",Toast.LENGTH_SHORT).show();
                        }
                    });

                    String[] subjects = NotenmanagementAPI.getSubjects();
                    if(subjects != null)    {
                        Intent intent = new Intent(MainActivity.this,SubjectsActivity.class);
                        intent.putExtra("Subjects",subjects);

                        MainActivity.this.startActivity(intent);
                    }

                } else  {
                    Log.i(TAG,"Failed");

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        }
        .start();
    }
}