package com.example.aecproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT=0;
    private static final int REQUEST_DISCOVER_BT=1;

    TextView status,pairt;
    ImageView bluetIc;
    Button onbtn,offbtn,cntbtn,pairbtn,membtn;

    BluetoothAdapter btadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);
        onbtn = findViewById(R.id.onBtn);
        offbtn = findViewById(R.id.offBtn);
        cntbtn = findViewById(R.id.cntbtn);
        pairbtn = findViewById(R.id.pairBtn);
        membtn = findViewById(R.id.membersBtn);
        bluetIc = findViewById(R.id.bluetoothIv);
        pairt = findViewById(R.id.pair);

        btadapter = BluetoothAdapter.getDefaultAdapter();

        if(btadapter==null)
            status.setText("BLUETOOTH IS NOT AVAILABLE ON THIS DEVICE");
        else
            status.setText("BLUETOOTH IS AVAILABLE ON THIS DEVICE");

        if(btadapter.isEnabled())
            bluetIc.setImageResource(R.drawable.ic_action_on);
        else
            bluetIc.setImageResource(R.drawable.ic_action_off);

        onbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!btadapter.isEnabled())
                    {
                        toastmssg("Turning on Bluetooth....");
                        //Intent to turn on Bluetooth
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent,REQUEST_ENABLE_BT);
                    }
                    else
                        toastmssg("Bluetooth is already on");
                }
        });



        offbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btadapter.isEnabled()){
                    btadapter.disable();
                    toastmssg("Turning Bluetooth off");
                    bluetIc.setImageResource(R.drawable.ic_action_off);
                    pairt.setText(" ");
                }
                else
                    toastmssg("Bluetooth is already off");
            }
        });


        cntbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, ConnectAlarm.class);
                startActivity(intent);
            }
        });

        pairbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btadapter.isEnabled()){
                    pairt.setText("Paired Devices");
                    Set<BluetoothDevice> devices = btadapter.getBondedDevices();
                    for(BluetoothDevice device:devices){
                        pairt.append("\nDevice"+device.getName()+","+device);
                    }
                }
                else  //bluetooth is off so can't get paired devices
                    toastmssg("Turn on Bluetooth to get paired devices.");
            }
        });

        membtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, TeamMembers.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode==RESULT_OK){
                    // bluetooth is on
                    bluetIc.setImageResource(R.drawable.ic_action_on);
                    toastmssg("Bluetooth is on");
                }
                else{
                    //user denied to turn on Bluetooth
                    toastmssg("Couldn't turn on Bluetooth");
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //show toast message
    private void toastmssg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
