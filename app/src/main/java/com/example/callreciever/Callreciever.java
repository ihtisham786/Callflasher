package com.example.callreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class Callreciever extends BroadcastReceiver {
    boolean flashon = false;
    boolean hascameraflash = false;
    Context c;
    boolean stop = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {


        c = context;
        hascameraflash = c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            showToast(context, "call started....");

            if (flashon) {
                flashon = true;
                try {
                    flashlightoff();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            CameraManager cameraManager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);
            String cameraid = null;
            try {
                cameraid = cameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            try {
                cameraManager.setTorchMode(cameraid, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }


            Toast.makeText(c, "Flashlight is of ", Toast.LENGTH_SHORT).show();

        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            showToast(context, "call ended...");


            if (flashon) {
                flashon = true;
                try {
                    flashlightoff();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                stop = true;
            }


            CameraManager cameraManager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);
            String cameraid = null;
            try {
                cameraid = cameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            try {
                cameraManager.setTorchMode(cameraid, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }



        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            showToast(context, "Incoming call...");
            if (hascameraflash) {
                if (flashon) {
                    flashon = false;
                    try {
                        flashlightoff();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    flashon = true;
                    try {
                        flashlighton();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }CameraManager cameraManager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);

                String myString = "0101010101010101010101010101010101010101010101010101010101010101010";

                long blinkDelay = 100; //Delay in ms

                for (int i = 0; i < myString.length()&& !stop;  i++) {
                    if (myString.charAt(i) == '0') {
                        try {
                            String cameraId = cameraManager.getCameraIdList()[0];

                            cameraManager.setTorchMode(cameraId, true);
                        } catch (CameraAccessException e) {
                        }
                    } else {
                        try {
                            String cameraId = cameraManager.getCameraIdList()[0];
                            cameraManager.setTorchMode(cameraId, false);
                        } catch (CameraAccessException e) {
                        }
                    }

                    try {
                        Thread.sleep(blinkDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }

        }
             else {
                 stop=true;
                Toast.makeText(c, "no flashlight available onn this divice", Toast.LENGTH_SHORT).show();
            }}


    void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashlighton() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);
        String cameraid = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraid, true);
        Toast.makeText(c, "Flashlight is on ", Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashlightoff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);
        String cameraid = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraid, false);


        Toast.makeText(c, "Flashlight is of ", Toast.LENGTH_SHORT).show();
    }

}



