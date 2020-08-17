package com.example.testing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class nameActivity extends AppCompatActivity {

    String email,password;
    EditText e5_name;
    CircleImageView circleImage;
    Uri resultUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        e5_name=(EditText) findViewById(R.id.editText5);
        circleImage=(CircleImageView) findViewById(R.id.circleImage);

        Intent myIntent=getIntent();
        if(myIntent!=null)
        {
            email=myIntent.getStringExtra("email");
            password=myIntent.getStringExtra("password");

        }
    }

    public void generateCode(View v)
    {
        Date myDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        String date = format1.format((myDate));
        Random r = new Random();

        int n = 100000 + r.nextInt(900000);
        String code = String.valueOf(n);

        String userName = e5_name.getText().toString();
        if(userName.equals("") && resultUrl== null)
        {
            Toast.makeText(getApplicationContext(),"Please Enter something ",Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent myIntent= new Intent(nameActivity.this,InviteCode.class);
            myIntent.putExtra("name",e5_name.getText().toString());
            myIntent.putExtra("email",email);
            myIntent.putExtra("password",password);
            myIntent.putExtra("date",date);
            myIntent.putExtra("isSharing","true");
            myIntent.putExtra("code",code);
            myIntent.putExtra("imageUri",resultUrl);


            startActivity(myIntent);
            finish();

        }

        //if(resultUrl != null)
        {


        }
        //else
        {
           // Toast.makeText(getApplicationContext(),"Please choose an image ",Toast.LENGTH_LONG).show();

        }

    }

    public void selectImage(View v)
    {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,12);
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 12 && resultCode == RESULT_OK && data != null)
        {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUrl = result.getUri();
                circleImage.setImageURI(resultUrl);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

