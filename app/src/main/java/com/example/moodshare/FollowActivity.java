package com.example.moodshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends AppCompatActivity {

    ArrayList<String> followersList=new ArrayList<String>();
    ListView followlistView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Follow Your Buddies");
        setContentView(R.layout.activity_follow);
        followlistView=(ListView)findViewById(R.id.followlistview);
        followlistView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,followersList);
        followlistView.setAdapter(adapter);
        followlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CheckedTextView checkedTextView=(CheckedTextView) view;

                if(checkedTextView.isChecked()){
                    ParseUser.getCurrentUser().add("following",followersList.get(i));
                }
                else
                {
                    Log.i("INFO",followersList.get(i)+" is NOT checked");
                    ParseUser.getCurrentUser().getList("following").remove(followersList.get(i));
                    List<String> tempUsers=ParseUser.getCurrentUser().getList("following");
                    ParseUser.getCurrentUser().remove("following");
                    ParseUser.getCurrentUser().put("following",tempUsers);

                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        ParseQuery<ParseUser> query= ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e==null && objects.size()>0){
                    for(ParseUser obj:objects){
                        followersList.add(obj.getUsername());

                    }
                    adapter.notifyDataSetChanged();

                    for (String user:followersList){
                        if(ParseUser.getCurrentUser().getList("following").contains(user)){
                            followlistView.setItemChecked(followersList.indexOf(user),true);
                        }

                    }
                }
            }


        });


    }
}