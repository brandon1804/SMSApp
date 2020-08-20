package sg.edu.rp.c346.id18044455.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etR;
    EditText etC;
    Button btnSend;
    Button btnSendV;
    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etR = findViewById(R.id.editR);
        etC = findViewById(R.id.editC);
        btnSend = findViewById(R.id.buttonSend);
        btnSendV = findViewById(R.id.buttonSendVia);

        checkPermission();

        br = new MessageReceiver();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipient = etR.getText().toString();
                String content = etC.getText().toString();
                String recipients [] = recipient.split(", *");
                SmsManager smsManager = SmsManager.getDefault();
                for(int i = 0; i < recipients.length; i++){
                    smsManager.sendTextMessage(recipients[i], null, content, null, null);
                }

                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                etC.setText("");
            }
        });

        btnSendV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipient = etR.getText().toString();
                Intent intentM = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto: " + recipient));
                startActivity(intentM);
            }
        });


    }//end of onCreate()

    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(br);
    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }

}//end of class