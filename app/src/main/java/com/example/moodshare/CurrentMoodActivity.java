package com.example.moodshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CurrentMoodActivity extends AppCompatActivity {

    ArrayList<String> moodlist=new ArrayList<String>();
    ArrayList<String> usermoodlist=new ArrayList<String>();
    ListView moodlistView;
    ArrayAdapter adapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.currentmood_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
                ParseUser.logOut();
                Intent intent=new Intent(CurrentMoodActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.followers:
                intent=new Intent(CurrentMoodActivity.this,FollowActivity.class);
                startActivity(intent);
                break;
            case R.id.buddies_mood:
                intent=new Intent(CurrentMoodActivity.this,BuddiesMood.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_mood);
        setTitle("Your Current Mood");
        moodlist.add("Happy");
        moodlist.add("Sad");
        moodlist.add("OKAY");

        moodlistView=(ListView)findViewById(R.id.moodlistView);
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,moodlist);
        adapter.notifyDataSetChanged();
        moodlistView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        moodlistView.setAdapter(adapter);
        moodlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView=(CheckedTextView) view;
                ParseObject moodObj=new ParseObject("Moods");
                ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("Moods");
                query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e==null){
                            Log.i("INFO","query found");
                            object.deleteInBackground();
                        }
                        else
                            Log.i("Error dele",e.getMessage());
                    }
                });


                if(checkedTextView.isChecked()){
                    Log.i("INFO",moodlist.get(i)+" is checked");
                    moodObj.put("username",ParseUser.getCurrentUser().getUsername());
                    usermoodlist.add(moodlist.get(i));
                    Log.i("usermoodlist",usermoodlist.toString());
                    moodObj.put("mood",usermoodlist);
                }
                else
                {
                    Log.i("INFO",moodlist.get(i)+" is unchecked");
                    moodObj.put("username",ParseUser.getCurrentUser().getUsername());
                    usermoodlist.remove(usermoodlist.indexOf(moodlist.get(i)));
                    moodObj.put("mood",usermoodlist);


                }
                moodObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null)
                            Toast.makeText(getApplicationContext(), "Successfull mood set",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });





        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("Moods");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null & objects.size()>0){
                    for (ParseObject mood : objects){
                        List<String> moodarray= (ArrayList<String>) mood.get("mood");


                        for (int i=0 ; i<moodarray.size();i++){
                            //Log.i("Moodar",moodarray.get(i));
                            if (moodlist.contains(moodarray.get(i))){
                                usermoodlist.add(moodarray.get(i));
                                moodlistView.setItemChecked(moodlist.indexOf(moodarray.get(i)),true);

                            }

                        }


                    }

                }
            }
        });



        }



    }
