package com.example.interfaz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 15/10/13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public class CambiarTarifa extends Activity {

    EditText edit;
    Button aceptar;
    Button cancelar;
    TextView tarifaActual;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiartarifa);
        edit = (EditText)findViewById(R.id.edit);
        aceptar = (Button)findViewById(R.id.aceptar);
        cancelar = (Button)findViewById(R.id.cancelar);
        tarifaActual =(TextView)findViewById(R.id.tarifa_actual);


        Bundle bundle = this.getIntent().getExtras();

        tarifaActual.setText("Tarifa actual: " + bundle.getString("actual") + " BsF");


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String temp_s = edit.getText().toString();
                    Double temp_d = Double.valueOf(temp_s);
                    if (temp_d<=0 || temp_s=="")
                    {

                        makeatoast("La tarifa debe ser mayor a cero");
                    }
                    else
                    {
                        String resultado = edit.getText().toString();
                        Intent  i = new Intent();
                        i.putExtra("RESULTADO", resultado);
                        setResult(RESULT_OK, i);
                        finish();

                    }
                    }
                catch (Exception e){
                    makeatoast("La tarifa debe ser mayor a cero");}


            }
        });


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  i = new Intent();
                setResult(RESULT_CANCELED, i);
                finish();
            }
        });

    }


    public void makeatoast(String mensaje)
    {
        Context context = getApplicationContext();
        String text = "Dispositivo no encontrado";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }




}
