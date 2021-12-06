package org.aplas.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.aplas.mylibrary.models.UserData;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    int bgColor;
    ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserData userdata = new UserData(getApplicationContext());
        if (userdata.isUserDataExist()) {
            Intent intent = new Intent(getApplicationContext(), InputDataActivity.class);
            startActivity(intent);
        }

        EditText editName = findViewById(R.id.editUserName)  ;
        EditText editCountry = findViewById(R.id.editCountry);
        EditText editPhone = findViewById(R.id.editPhone);

        Button btnPickColor = findViewById(R.id.btnPickBgColor);
        Button btnSaveUser = findViewById(R.id.btnSaveUser);
        layout = findViewById(R.id.mainLayout);

        bgColor = getResources().getColor(R.color.bgMain);
        layout.setBackgroundColor(bgColor);

        btnPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                openColorPicker();
            }
        });

        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String country = editCountry.getText().toString();
                String phone = editPhone.getText().toString();
                if ((name.length()>0) && (country.length()>0) && (phone.length()>0))  {
                    userdata.saveUserData(name,country,phone,bgColor);
                    Intent intent = new Intent(getApplicationContext(), InputDataActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Name, country, and phone number must be filled!!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, bgColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                bgColor = color;
                layout.setBackgroundColor(bgColor);
            }
        });
        colorPicker.show();
    }


}