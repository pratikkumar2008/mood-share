package com.example.moodshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuddiesMood extends AppCompatActivity {

    ListView buddiesListview;
    List<Map<String,String>> buddiesDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddies_mood);
        setTitle("Your Buddies");

        ParseQuery<ParseObject> query= ParseQuery.getQuery("Moods");
        query.whereContainedIn("username",ParseUser.getCurrentUser().getList("following"));
        query.orderByDescending("createdAt");
        query.setLimit(20);
        buddiesListview=(ListView)findViewById(R.id.buddiesmoodlistview);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> moods, ParseException e) {
                if (e==null){

                    for (ParseObject mood : moods) {
                        Map<String, String> moodInfo = new HashMap<String, String>();
                        ArrayList<String> buddiesmood= (ArrayList<String>) mood.get("mood");
                        String moodinstring=buddiesmood.toString();

                        moodInfo.put("content", moodinstring);
                        moodInfo.put("username", mood.getString("username"));
                        buddiesDataList.add(moodInfo);
                        SimpleAdapter simpleAdapter = new SimpleAdapter(BuddiesMood.this, buddiesDataList, android.R.layout.simple_list_item_2,
                                new String[]{"content", "username"}, new int[]{android.R.id.text2, android.R.id.text1});
                        buddiesListview.setAdapter(simpleAdapter);
                    }


                }
                else {
                    Log.i("Error",e.getMessage());
                }

            }
        });


    }
}