package com.tiringbring.roomdbtest;


import java.io.File;

import java.util.ArrayList;

import java.util.List;

import android.app.AlertDialog;

import android.app.ListActivity;

import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;

import android.os.Environment;
import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


public class FileExplorerActivity extends AppCompatActivity {


    private List<String> item = null;
    private List<String> path = null;
    private String root = "/";
    private TextView myPath;
    private RecyclerView rcFiles;
    private RecyclerView.Adapter adapter;


    /**
     * Called when the activity is first created.
     */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_explorer);

        myPath = (TextView) findViewById(R.id.path);

        getDir(Environment.getExternalStorageDirectory().toString()+root);

    }


    private void getDir(String dirPath) {

        myPath.setText("Location: " + dirPath);

        String filePath = Environment.getExternalStorageDirectory().toString()+dirPath;
        item = new ArrayList<String>();

        path = new ArrayList<String>();


        File f = new File(dirPath);

        File[] files = f.listFiles();


        if (!dirPath.equals(root)) {


            item.add(root);

            path.add(root);


            item.add("../");

            path.add(f.getParent());


        }


        for (int i = 0; i < files.length; i++) {

            File file = files[i];

            path.add(file.getPath());

            if (file.isDirectory())

                item.add(file.getName() + "/");

            else

                item.add(file.getName());

        }


        //ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.file_row, item);
        //setListAdapter(fileList);
        rcFiles = (RecyclerView) findViewById(R.id.rcFiles);
        rcFiles.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FileExplorerAdapter(item, this);
        rcFiles.setAdapter(adapter);

    }




    public void onListItemClick(int position) {
        File file = new File(path.get(position));
        if (file.isDirectory()) {
            if (file.canRead())
                getDir(path.get(position));
            else {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.icon)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                    }
                                }).show();
            }
        } else {
//            new AlertDialog.Builder(this)
//                    .setIcon(R.drawable.icon)
//                    .setTitle("[" + file.getName() + "]")
//                    .setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // TODO Auto-generated method stub
//                                }
//                            }).show();
            MainActivity.personDatabase.close();
            MainActivity.importDB(path.get(position).substring(Environment.getExternalStorageDirectory().toString().length()), getApplicationContext());
            MainActivity.personDatabase = Room.databaseBuilder(getApplicationContext(), PersonDatabase.class, "Persondb").allowMainThreadQueries().build();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }

}