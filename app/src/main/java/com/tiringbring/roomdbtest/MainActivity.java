package com.tiringbring.roomdbtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static PersonDatabase personDatabase;
    private EditText editText;
    private Button button, btnExport, btnImport, btnFileExplorer;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        personDatabase = Room.databaseBuilder(getApplicationContext(), PersonDatabase.class, "Persondb").allowMainThreadQueries().build();
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        btnExport = findViewById(R.id.btnExport);
        btnImport = findViewById(R.id.btnImport);
        btnFileExplorer = findViewById(R.id.btnFileExplorer);
        textView = findViewById(R.id.textview);
        loadNamestoTextView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")){
                    Person person = new Person();
                    person.firstName = editText.getText().toString();
                    personDatabase.personDao().insertAll(person);
                    loadNamestoTextView();
                }
            }
        });
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File direct = new File(Environment.getExternalStorageDirectory() + "/RoomDB");

                if(!direct.exists())
                {
                    if(direct.mkdir())
                    {
                        //directory is created;
                    }

                }
                personDatabase.close();
                exportDB();
                personDatabase = Room.databaseBuilder(getApplicationContext(), PersonDatabase.class, "Persondb").allowMainThreadQueries().build();
                //importDB();
            }
        });
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //personDatabase.close();
                startActivity(new Intent(getApplicationContext(), FileExplorerActivity.class));
                //importDB("/RoomDB/Persondb", getApplicationContext());
                //personDatabase = Room.databaseBuilder(getApplicationContext(), PersonDatabase.class, "Persondb").allowMainThreadQueries().build();
                //loadNamestoTextView();
            }
        });
        btnFileExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FileExplorerActivity.class));
            }
        });

    }
    private void loadNamestoTextView(){
        List<Person> personList = personDatabase.personDao().getAll();
        String text = "";
        for(int i = 0; i<personList.size();i++){
            text += personList.get(i).firstName+"\n" ;
        }
        textView.setText(text);
    }
    public static void importDB(String backupPath, Context context) {
        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data  = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + "com.tiringbring.roomdbtest"
                        + "//databases//" + "Persondb";
                String backupDBPath  =  backupPath;//"/RoomDB/Persondb";
                File  backupDB= new File(data, currentDBPath);
                File currentDB  = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText( context, backupDB.toString(),
                        Toast.LENGTH_LONG).show();


            }
        } catch (Exception e) {

            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }
    //exporting database
    private void exportDB() {
        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + "com.tiringbring.roomdbtest"
                        + "//databases//" + "Persondb";
                String backupDBPath  = "/RoomDB/Persondb";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getBaseContext(), backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }


}
