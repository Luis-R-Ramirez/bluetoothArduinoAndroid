package com.example.interfaz;

/**
 * Created by Luis on 12/11/13.
 */
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Seleccionar extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;

    ListView listDevicesFound;
    Button btnScanDevice;
    TextView stateBluetooth;
    BluetoothAdapter bluetoothAdapter;
    Button Explorar;
    TextView mensaje;

    ArrayAdapter<String> btArrayAdapter;
    ArrayList<String> nombres = new ArrayList<String>();
    ArrayList <String>list = new ArrayList <String>(100);    // can store any Nunber


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccionar);
       // Explorar = (Button)findViewById(R.id.Explorar);
       // mensaje = (TextView)findViewById(R.id.mensaje);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices
                = bluetoothAdapter.getBondedDevices();

        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<String>(Seleccionar.this, android.R.layout.simple_list_item_1);
        //  listDevicesFound.setAdapter(btArrayAdapter);
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceBTName = device.getName();
                nombres.add(deviceBTName);
                list.add(deviceBTName);
                String deviceBTMajorClass
                        = getBTMajorDeviceClass(device
                        .getBluetoothClass()
                        .getMajorDeviceClass());
                btArrayAdapter.add(deviceBTName + "\n"
                        + deviceBTMajorClass);
            }
        }
         listDevicesFound.setAdapter(btArrayAdapter);

        ArrayList <String>list = new ArrayList <String>(100);    // can store any Nunber
        list.add("Hello");
        list.add("world");
        Iterator iter = list.iterator();

        /*
        P
         */

        listDevicesFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                String s = nombres.get(position);

                //mensaje.setText(s);

                Intent  i = new Intent();
                i.putExtra("DISPOSITIVO", s);
                setResult(RESULT_OK, i);
                finish();

            }




        });



        /*
         F
         */

    }

    private String getBTMajorDeviceClass(int major){
        switch(major){
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return "AUDIO_VIDEO";
            case BluetoothClass.Device.Major.COMPUTER:
                return "COMPUTER";
            case BluetoothClass.Device.Major.HEALTH:
                return "HEALTH";
            case BluetoothClass.Device.Major.IMAGING:
                return "IMAGING";
            case BluetoothClass.Device.Major.MISC:
                return "MISC";
            case BluetoothClass.Device.Major.NETWORKING:
                return "NETWORKING";
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "PERIPHERAL";
            case BluetoothClass.Device.Major.PHONE:
                return "PHONE";
            case BluetoothClass.Device.Major.TOY:
                return "TOY";
            case BluetoothClass.Device.Major.UNCATEGORIZED:
                return "UNCATEGORIZED";
            case BluetoothClass.Device.Major.WEARABLE:
                return "AUDIO_VIDEO";
            default: return "unknown!";
        }
    }







    public void buscar(View v){
        listDevicesFound.setAdapter(btArrayAdapter);

    }

    public void limpiar(View v)
    {
        //btArrayAdapter.clear();
        listDevicesFound.clearChoices();

        // listDevicesFound.setAdapter(btArrayAdapter);
    }





}


