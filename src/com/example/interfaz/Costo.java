package com.example.interfaz;

/**
 * Created with IntelliJ IDEA.
 * User: Luis
 * Date: 14/10/13
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Costo extends Activity {


    TextView consumo_txt;
    TextView proyeccion;

    Button calcular;

    double consumo;

    int dias;
    int horas;


    String prueba;
    String tarifa_s;
    double tarifa_d;
    double costo_proyeccion;


    private Spinner diasOpciones;
    private Spinner horasOp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.costo);
        consumo_txt = (TextView)findViewById(R.id.consumo);
        proyeccion = (TextView)findViewById(R.id.proyeccion_int);
        Bundle extras = getIntent().getExtras();
        prueba = extras.getString("consumo");
        tarifa_s = extras.getString("tarifa");
        diasOpciones = (Spinner)findViewById(R.id.dias);
        horasOp = (Spinner)findViewById(R.id.horas);
        calcular = (Button)findViewById(R.id.calcular);
        consumo = Double.valueOf(prueba);
        tarifa_d = Double.valueOf(tarifa_s);
        consumo_txt.setText("consumo: " + prueba);



        final String[] datos =
                new String[]{"Dia","Semana","Mes","año"};

        final String[] horas_s =
                new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, datos);


        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        diasOpciones.setAdapter(adaptador);




        diasOpciones.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        switch(position) {
                            case 0:
                                dias = 1;

                                break;
                            case 1:
                                dias = 7;
                                break;
                            case 2:
                                dias = 30;
                                break;
                            case 3:
                                dias = 365;
                                break;
                            default:
                                dias = 1;
                                break;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



        /// Segundo spinner

        ArrayAdapter<String> adaptadorHoras =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, horas_s);


        adaptadorHoras.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        horasOp.setAdapter(adaptadorHoras);



        horasOp.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        switch(position) {
                            case 0:
                                horas = 1;
                                 break;
                            case 1:
                                horas = 2;
                                break;
                            case 2:
                                horas = 3;
                                break;
                            case 3:
                                horas = 4;
                                break;
                            case 4:
                                horas = 5;
                                break;
                            case 5:
                                horas = 6;
                                break;
                            case 6:
                                horas = 7;
                                break;
                            case 7:
                                horas = 8;
                                break;
                            case 8:
                                horas = 9;
                                break;
                            case 9:
                                horas = 10;
                                break;
                            case 10:
                                horas = 11;
                                break;
                            case 11:
                                horas = 12;
                                break;
                            case 12:
                                horas = 13;
                                break;
                            case 13:
                                horas = 14;
                                break;
                            case 14:
                                horas = 15;
                                break;
                            case 15:
                                horas = 16;
                                break;
                            case 16:
                                horas = 17;
                                break;
                            case 17:
                                horas = 18;
                                break;
                            case 18:
                                horas = 19;
                                break;
                            case 19:
                                horas = 20;
                                break;
                            case 20:
                                horas = 21;
                                break;
                            case 21:
                                horas = 22;
                                break;
                            case 22:
                                horas = 23;
                                break;
                            case 23:
                                horas = 24;
                                break;

                            default:
                                horas = 1;
                                break;
                        }

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //
                    }
                });


        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ss =  calcularCosto(tarifa_d,consumo,dias,horas);
                proyeccion.setText(String.valueOf(ss) + "BsF");
            }
        });

    }

    public String calcularCosto(double tarifa, double consumo, int dias, int horas )
    {
        double costo = tarifa*consumo*dias*horas;
        String costo_S = String.valueOf(costo);
        return    costo_S;
    }

}

