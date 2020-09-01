package com.example.moodshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    boolean isSignupActive=true;


    public void redirect(){
        if(ParseUser.getCurrentUser()!=null && ParseUser.getCurrentUser().getUsername()!=null) {
            Intent intent = new Intent(getApplicationContext(), CurrentMoodActivity.class);
            startActivity(intent);
        }


    }

    public void signuporlogin(View view){

        EditText usernameEditText=(EditText)findViewById(R.id.username);
        EditText passwordEditText=(EditText)findViewById(R.id.password);
        String username=usernameEditText.getText().toString();
        String password=passwordEditText.getText().toString();



        if (isSignupActive){
            ParseUser user=new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        Toast.makeText(getApplicationContext(),"Signup successful",Toast.LENGTH_SHORT).show();
                        redirect();
                    }
                    else
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });

        }
        else{
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e==null && user!=null){
                        Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
                        redirect();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }

    public void toggleLogin(View view){
        Button signup=(Button)findViewById(R.id.signup);
        TextView login=(TextView) findViewById(R.id.login);

        if (isSignupActive){
            isSignupActive=false;
            signup.setText("Login");
            login.setText("OR, Sign Up");

        }
        else {
            isSignupActive=true;
            signup.setText("Sign Up");
            login.setText("OR, Login");
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}