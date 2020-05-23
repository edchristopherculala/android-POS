package com.ed.chris.androidpos.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.AdminMain;


public class AdminLogin extends AppCompatActivity {

    private EditText inputAdminUsername, inputAdminPassword;
    private ImageView loginAdminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminLogin();

    }

    private void adminLogin(){

        inputAdminUsername = findViewById(R.id.inputEditTextAdminUsername);
        inputAdminPassword = findViewById(R.id.inputEditTextAdminPassword);

        final String masterAdminUsername, masterAdminPassword;

        masterAdminUsername = "admin";
        masterAdminPassword = "1";

        loginAdminButton = findViewById(R.id.imageViewButtonAdminLogin);


        loginAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String adminUsername = inputAdminUsername.getText().toString().trim();
                String adminPassword = inputAdminPassword.getText().toString().trim();

                if (adminUsername.isEmpty() && adminPassword.isEmpty()){

                    Toast.makeText(AdminLogin.this,"Please Input Username and Password", Toast.LENGTH_LONG).show();

                }
                else if (adminUsername.isEmpty()){

                    Toast.makeText(AdminLogin.this,"Please Input Username", Toast.LENGTH_LONG).show();

                }else if (adminPassword.isEmpty()){

                    Toast.makeText(AdminLogin.this,"Please Input Password", Toast.LENGTH_LONG).show();

                }else if (!adminUsername.equals(masterAdminUsername) && adminPassword.equals(masterAdminPassword)){

                    Toast.makeText(AdminLogin.this,"Account credentials not found!", Toast.LENGTH_LONG).show();

                }else if (adminUsername.equals(masterAdminUsername) && !adminPassword.equals(masterAdminPassword)){

                    Toast.makeText(AdminLogin.this,"Account credentials not found!", Toast.LENGTH_LONG).show();

                }else if (!adminUsername.equals(masterAdminUsername) && !adminPassword.equals(masterAdminPassword)){

                    Toast.makeText(AdminLogin.this,"Account credentials not found!", Toast.LENGTH_LONG).show();

                } else if (adminUsername.equals(masterAdminUsername) && adminPassword.equals(masterAdminPassword)){

                    Intent intent = new Intent(AdminLogin.this, AdminMain.class);
                    startActivity(intent);

                }



            }
        });





    }
}
