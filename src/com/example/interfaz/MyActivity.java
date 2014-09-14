package com.example.interfaz;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.bluetooth.BluetoothClass;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket mmSocket;
    public BluetoothDevice mmDevice;
    public OutputStream mmOutputStream;
    public InputStream mmInputStream;
    public Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;



    private ListView listView;
    private ProgressDialog progressDialog;
    String temp = "100";
    String tarifa_s = "100";
    private final static int solicitud = 1;
    ArrayList<String> nombres = new ArrayList<String>();

    boolean conectado = false;
    ArrayAdapter<String> btArrayAdapter;
    TextView mensaje;
    String nombre_dispositivo = "No ha seleccionado un dispositivo";
    String estado = "Donectado";

    View barra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mensaje = (TextView)findViewById(R.id.mensaje1);
        barra = (View)findViewById(R.id.barra);
        mensaje.setText("Dispositivo seleccionado: " +  nombre_dispositivo + " " + '\n' + "Estado: " + estado);
        this.listView = (ListView) findViewById(R.id.listView);
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(R.drawable.conectar, "Conectar"));
        items.add(new Item(R.drawable.calcular_costo, "Calcular costo"));
        items.add(new Item(R.drawable.actualizar, "Actualizar tarifa"));
        items.add(new Item(R.drawable.icon3, "Acerca de"));
        items.add(new Item(R.drawable.seleccionar, "Seleccionar dispositivo"));
        items.add(new Item(R.drawable.ayuda, "ayuda"));
        items.add(new Item(R.drawable.salir, "Salir"));


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices
                = mBluetoothAdapter.getBondedDevices();


        btArrayAdapter = new ArrayAdapter<String>(MyActivity.this, android.R.layout.simple_list_item_1);
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceBTName = device.getName();
                nombres.add(deviceBTName);
                String deviceBTMajorClass
                        = getBTMajorDeviceClass(device
                        .getBluetoothClass()
                        .getMajorDeviceClass());
                btArrayAdapter.add(deviceBTName + "\n"
                        + deviceBTMajorClass);
            }
        }
        this.listView.setAdapter(new ItemAdapter(this, items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                if (position==0)
                {
                    try
                    {
                        findBT();
                        openBT();

                    }


                    catch (Exception ex) {makeatoast("La conexion ha fallado");

                    }



                }
                if (position==1)
                {


                    // Envia "M" (Medir consumo)
                    try
                    {
                        sendData("M");
                    }
                    catch (Exception e)


                    {
                        estado = "desconectado";
                        barra.setBackgroundColor(Color.GRAY);
                        makeatoast("Debe estar conectado para calcular el costo");

                    }



                }

                if (position==2)
                {

                    Intent intent = new
                            Intent(MyActivity.this, CambiarTarifa.class);
                    Bundle b = new Bundle();
                    b.putString("actual", tarifa_s);
                    intent.putExtras(b);
                    startActivityForResult(intent, solicitud);

                }


                if (position==3)
                {

                    Intent intent =
                            new Intent(MyActivity.this, Acercade.class);
                    startActivity(intent);
                }
                if (position==4)
                {
                    Intent intent = new
                            Intent(MyActivity.this, Seleccionar.class);
                    startActivityForResult(intent, 2);
                }


                if (position==5)
                {

                    Intent intent =
                            new Intent(MyActivity.this, Ayuda.class);
                    startActivity(intent);
                }


                if (position==6)
                {
                    closeBT();
                    conectado = false;
                    barra.setBackgroundColor(Color.GRAY);
                    finish();
                }

            }



        });




}

    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {

        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(nombre_dispositivo))
                {
                    mmDevice = device;
                    break;
                }
            }
        }

    }



    void openBT() throws IOException
    {
        final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        //try{
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
       // }
      //  catch (Exception device) {makeatoast("msoket error");
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        beginListenForData();
        // Envia "C" (Conectar)
        estado = "conectado";
        barra.setBackgroundColor(Color.GREEN);
        mensaje.setText("Dispositivo seleccionado: " +  nombre_dispositivo + " " + '\n' + "Estado: " + estado);
        sendData("C");
    }

    public void makeatoast(String mensaje)
    {
        Context context = getApplicationContext();
        String text = "Dispositivo no encontrado";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }


    void closeBT()
    {
        try
        {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            conectado = false;


        }
        catch (Exception e){}
    }




    void sendData(String mensaje) throws IOException
    {
        mensaje += "\n";
        mmOutputStream.write(mensaje.getBytes());

    }


    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        handler.post(new Runnable()
                        {
                            public void run()
                            {


                            }
                        });
                        int bytesAvailable = mmInputStream.available();
                        if(mmInputStream.available() > 0)
                        {
                            handler.post(new Runnable()
                            {
                                public void run()
                                {

                                }
                            });
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)

                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {

                                                if(conectado==true)
                                                {

                                                    Intent intent =
                                                            new Intent(MyActivity.this, Costo.class);
                                                    intent.putExtra("consumo", data);
                                                    intent.putExtra("tarifa", tarifa_s);

                                                    startActivity(intent);


                                                }
                                                if (conectado==false)
                                                {
                                                    tarifa_s = data;
                                                    conectado = true;

                                                }

                                        }
                                    });
                                }
                                else
                                {

                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK) {


            tarifa_s = data.getExtras().getString("RESULTADO");
            try
            {
                sendData(tarifa_s);
            }
            catch (Exception e){   makeatoast("tarifa no enviada"); }


        }
        if (requestCode==2 && resultCode==RESULT_OK){

            String temp = data.getExtras().getString("DISPOSITIVO");
            if(temp != nombre_dispositivo)
            {

                nombre_dispositivo = temp;
                closeBT();
                estado = "desconectado";
                barra.setBackgroundColor(Color.GRAY);
                mensaje.setText("Dispositivo seleccionado: " +  nombre_dispositivo + " " + '\n' + "Estado: " + estado);


            }
        }

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

    }



}
