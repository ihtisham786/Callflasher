package com.example.callreciever;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView powerof;

    boolean hascameraflash = false;
    boolean flashon = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        powerof = findViewById(R.id.poweroff);
        hascameraflash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);

        }

        powerof.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(hascameraflash){
                    if(flashon){
                        flashon = false;
                        powerof.setImageResource(R.drawable.off);
                        try {
                            flashlightoff();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        flashon = true;
                        powerof.setImageResource(R.drawable.on);
                        try {
                            flashlighton();

                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "no flashlight available onn this divice", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashlighton() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraid = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraid , true);
//        Toast.makeText(MainActivity.this, "Flashlight is on ", Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashlightoff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraid = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraid , false);

//        Toast.makeText(MainActivity.this, "Flashlight is of ", Toast.LENGTH_SHORT).show();
    }
}



