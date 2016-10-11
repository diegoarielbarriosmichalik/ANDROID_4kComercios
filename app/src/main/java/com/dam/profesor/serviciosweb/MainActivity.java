package com.dam.profesor.serviciosweb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*String tabla = "alumno";*/
    String tabla = "usuario";

    Button consultarporid;
    EditText idendificador;
    EditText nombre;
    EditText direccion;
    TextView resultado;
    String IP = "http://municipalidaddecarmendelparana.com";
    String GET_BY_ID = IP + "/obtener_" + tabla + "_por_id.php";
    ObtenerWebService hiloconexion;

    boolean ok = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        consultarporid = (Button)findViewById(R.id.consultar);
        idendificador = (EditText)findViewById(R.id.eid);
        nombre = (EditText)findViewById(R.id.enombre);
        direccion = (EditText)findViewById(R.id.edireccion);
        resultado = (TextView)findViewById(R.id.resultado);
        consultarporid.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.consultar:
                hiloconexion = new ObtenerWebService();
                String cadenallamada = GET_BY_ID + "?nombre=" + nombre.getText().toString()+"&pass="+direccion.getText().toString();
                hiloconexion.execute(cadenallamada,"2");
                System.err.println(cadenallamada);
                break;
            default:
                break;
        }
    }

    public class ObtenerWebService extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;
            String devuelve ="";

          if(params[1]=="2"){

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){
                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON
                        if (resultJSON=="1"){      // hay un alumno que mostrar
                           /* devuelve = devuelve + respuestaJSON.getJSONObject(tabla).getString("idUsuario") + " " +
                                    respuestaJSON.getJSONObject(tabla).getString("nombre") + " " +
                                    respuestaJSON.getJSONObject(tabla).getString("contrasenha");*/
                            ok = true;

                       }

                        if(ok == true){
                            System.err.println("Encontrado "+ok);
                        }else{
                            System.err.println("Encontrado "+ok);
                        }
                        ok = false;

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;
            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            resultado.setText(s);
            //super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
