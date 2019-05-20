package com.example.mainactivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.UUID;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_READ_PHONE_STATE = 1100;
    private TextView outputUUID;
    // TextView 값을 입력할 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputUUID = (TextView)findViewById(R.id.outputUUID);
        //outputUUID 라고 이름이 명시된 TextView, outputUUID 선언

        System.out.println("출력중");

        //권한 설정
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        //권한 설정이 되어있을때.
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //GetDevicesUUID 실행
           GetDevicesUUID(MainActivity.this);
        }




        bindViews();
        setupEvents();
        setValues();

    }

    //UUID를 가져오는 함수
    private String GetDevicesUUID(Context mContext) throws SecurityException {
        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        // 그냥 실행시 권한 문제때문에 제대로 된 실행이 되지 않음.
        /* 1. AndroidManifest.xml 에 권한 부여
           2. OnCreate 실행문에
           int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
           if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
           } else {
           }
           권한 체크 문장 밑 실행시킬 준비.
           else 문에  GetDevicesUUID(MainActivity.this); 입력.

           위 문장 외에도

           public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
             case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    GetDevicesUUID(MainActivity.this);
                }
                break;
             default:
                break;
            }
             super.onRequestPermissionsResult(requestCode, permissions, grantResults);
          }
          함수를 밑에 추가.
          if문에 실행 할수있게 GetDevicesUUID(MainActivity.this); 넣어야 한다.
         */
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        System.out.println("device id : "+deviceId);
        outputUUID.setText(deviceId);
        return deviceId;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    GetDevicesUUID(MainActivity.this);
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setupEvents() {

    }

    @Override
    public void setValues() {

    }

    @Override
    public void bindViews() {

    }
}
