package com.example.feealerter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etMessage = findViewById(R.id.et_message);
        final EditText etPhoneNumbers = findViewById(R.id.et_phone_numbers);
        Button btnSend = findViewById(R.id.btn_send);

        checkForSmsPermission();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                String phoneNumbers = etPhoneNumbers.getText().toString();

                if (!message.isEmpty() && !phoneNumbers.isEmpty()) {
                    sendSms(message, phoneNumbers);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter both message and phone numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkForSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user asynchronously
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with sending SMS
            } else {
                // Permission denied, disable the functionality that depends on this permission
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSms(String message, String phoneNumbers) {
        SmsManager smsManager = SmsManager.getDefault();
        String[] numbers = phoneNumbers.split(",");
        for (String number : numbers) {
            String prefix = number.substring(0, 3);
            if (!prefix.equals("+91")) {
                String finalNumber = "+91" + number;
                smsManager.sendTextMessage(finalNumber.trim(), null, message, null, null);
            } else {
                smsManager.sendTextMessage(number.trim(), null, message, null, null);
            }
        }
        Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
    }
}
