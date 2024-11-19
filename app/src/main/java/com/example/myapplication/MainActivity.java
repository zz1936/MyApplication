package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

            
        });


        initUI();
    }

    private void initUI() {

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.pasword_input);

        loginbtn = findViewById(R.id.login_btn);

        loginbtn.setOnClickListener(username = usernameInput.toString(), password = passwordInput.toString(),
        Log.i("Text Credentials", MessageFormat.format("Username : {0} and password : {1}", usernameInput.toString(), passwordInput.toString())));
    }
}

