package com.example.project;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity3 extends AppCompatActivity {
    private List<String> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        EditText editText = findViewById(R.id.edit_text);
        Button submit = findViewById(R.id.add_button);
        ListView listView = findViewById(R.id.list_view);
        MyListAdapter  adapter = new MyListAdapter(itemList, this);
        listView.setAdapter(adapter);
     SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
      Set<String> savedItems = sharedPreferences.getStringSet("MyItems", new HashSet<String>());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("MyItems");
        itemList.addAll(savedItems);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                getIntent().getStringExtra("position");
                    if (!input.isEmpty()) {
                        itemList.add(input);
                        adapter.notifyDataSetChanged();
                        editText.setText("");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putStringSet("MyItems", new HashSet<String>(itemList));
                        editor.apply();
                    }

            }
        });

    }


    public class MyListAdapter extends BaseAdapter {

        private List<String> itemList;
        private Context context;

        public MyListAdapter(List<String> itemList, Context context) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public int getCount() {

            return itemList.size();
        }

        @Override
        public Object getItem(int position) {

            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout2, null);
            } else {
                view = convertView;
            }

            TextView itemText = view.findViewById(R.id.text_view);
            FloatingActionButton delete = view.findViewById(R.id.floatingActionButton);
            LinearLayout layout = view.findViewById(R.id.linear);
            itemText.setText(itemList.get(position));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity3.this);
                    alertDialogBuilder.setMessage("Are you sure, You want to Delete this ?");
                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Toast.makeText(MainActivity3.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            View childView = findViewById(R.id.child);
                            layout.removeView(childView);
                        }
                                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

            return view;
        }
    }

    }








