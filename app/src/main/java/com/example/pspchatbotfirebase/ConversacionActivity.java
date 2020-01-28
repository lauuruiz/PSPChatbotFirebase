package com.example.pspchatbotfirebase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.pspchatbotfirebase.apibot.ChatterBot;
import com.example.pspchatbotfirebase.apibot.ChatterBotFactory;
import com.example.pspchatbotfirebase.apibot.ChatterBotSession;
import com.example.pspchatbotfirebase.apibot.ChatterBotType;
import com.example.pspchatbotfirebase.apibot.Utils;
import com.example.pspchatbotfirebase.firebase.ChatSentence;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConversacionActivity extends AppCompatActivity implements OnInitListener{

    //para que el bot hable
    private TextToSpeech mensaje;
    private Button btHablar;

    //para hablar con el bot
    private Button btDecir;
    private EditText etMensaje;
    private TextView tvBot, tvYo, tvNombre;
    private String noTraducido, traducido;
    private String hablante;
    private String usuario;

    //para las fechas
    private Calendar c2 = new GregorianCalendar();

    //mensaje usuario
    private String usuEs, usuEn;
    //mensaje bot
    private String botEs, botEn;

    private String nombre;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversacion);

        usuario = getIntent().getStringExtra("nombre");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseAuth.getInstance().signOut();
            }
        });

        initComponentsChatBot();
        initEvents();


        Bundle datos = this.getIntent().getExtras();
        nombre = datos.getString("nombre");
        String[] cadena = nombre.split("\\@");
        nombre = cadena[0];
        tvNombre.setText(nombre);


        Log.v("xyz", "bthablar usuEs: "+usuEs+" usuEn: "+usuEn);
        if(usuEs != null && usuEn != null){
            initFireBase(hablante);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.historial) {
            Intent i = new Intent(this, HistorialActivity.class);
            i.putExtra("nombre", nombre);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponentsChatBot(){
        mensaje = new TextToSpeech(this, this);
        btHablar = findViewById(R.id.btHablar);

        etMensaje = findViewById(R.id.etMensaje);
        tvBot = findViewById(R.id.tvBot);
        tvYo = findViewById(R.id.tvYo);
        tvNombre = findViewById(R.id.tvNombre);
        btDecir = findViewById(R.id.btDecir);
    }

    private void initEvents(){
        //para que el bot hable
        btDecir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decir();
            }
        });

        //para hablar con el bot
        btHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noTraducido = etMensaje.getText().toString();
                tvYo.setText(noTraducido);

                etMensaje.setText("");
                TraduccionIngles ingles = new TraduccionIngles(noTraducido);
                ingles.execute();
            }
        });
    }

    //Para que el bot hable
    @Override
    protected void onDestroy() {
        if(mensaje != null){
            mensaje.stop();
            mensaje.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int result = mensaje.setLanguage(Locale.getDefault());

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.v("onInit", "El idioma no está disponible");
            }else{
                btDecir.setEnabled(true);
                decir();
            }
        }else{
            Log.v("onInit", " TextToSpeech no funciona");
        }
    }

    private void decir(){
        String texto = etMensaje.getText().toString();
        mensaje.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
    }

    //Para hablar con el bot
    private String chat(String s){
        String bot ="";
        try{
            //creamos al bot y le pasamos el mensaje en s
            ChatterBotFactory factory = new ChatterBotFactory();

            ChatterBot bot1 = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            ChatterBotSession bot1session = bot1.createSession();

            bot = bot1session.think(s);

            botEn = bot;
            TraduccionEspanol espanol = new TraduccionEspanol(bot);
            espanol.execute();
        }catch(Exception e){
            Log.v("xyz", "Error, el bot no va a hablar.");
        }
        return bot;
    }

    private class Chat extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String chat = chat(traducido);
            botEn = chat;
            return chat;
        }
    }

    //https://www.bing.com/ttranslatev3?isVertical=1&&IG=4FD6F42132A742E7A31D8F58977C7B9F&IID=translator.5026.1
    //https://www.bing.com/ttranslatev3?IID=translator.5026.1
    //POST
    //&fromLang=es&text=soy%20programador&to=en
    //fromLang: es
    //text: soy programador
    //to: en
    //useragent Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0ElMio

    public String decomposeJson(String json){
        String translationResult = "Could not get";
        try {
            JSONArray arr = new JSONArray(json);
            JSONObject jObj = arr.getJSONObject(0);
            translationResult = jObj.getString("translations");
            JSONArray arr2 = new JSONArray(translationResult);
            JSONObject jObj2 = arr2.getJSONObject(0);
            translationResult = jObj2.getString("text");
        } catch (JSONException e) {
            translationResult = e.getLocalizedMessage();
        }
        return translationResult;
    }

    private class TraduccionIngles extends AsyncTask<Void, Void, Void> {
        private final Map<String, String> headers;
        private final Map<String, String> vars;
        String mensajeIngles = "Error al traducir al ingles";

        private TraduccionIngles(String mensajeEsp) {
            headers = new LinkedHashMap<String, String>();
            headers.put("Content-type", "application/x-www-form-urlencoded");
            headers.put("User-Agent:", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");

            vars = new HashMap<String, String>();
            vars.put("fromLang", "es");
            vars.put("text", mensajeEsp);
            vars.put("to", "en");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //traduce al ingles
                mensajeIngles = Utils.performPostCall("https://www.bing.com/ttranslatev3", (HashMap) vars);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("xyz", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            traducido = decomposeJson(mensajeIngles);
            usuEn = traducido;
            usuEs = noTraducido;

            Log.v("xyz", "bthablar usuEs: "+usuEs+" usuEn: "+usuEn);
            if(usuEs != null && usuEn != null){
                initFireBase(usuario);
            }
            new Chat().execute();
        }
    }

    private class TraduccionEspanol extends AsyncTask<Void, Void, Void>{

        private final Map<String, String> headers;
        private final Map<String, String> vars;
        String mensajeEspanol = "Error";

        private TraduccionEspanol(String mensajeEng) {
            headers = new LinkedHashMap<String, String>();
            headers.put("Content-type","application/x-www-form-urlencoded");
            headers.put("User-Agent:","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");

            vars = new HashMap<String, String>();
            vars.put("fromLang", "en");
            vars.put("text",mensajeEng);
            vars.put("to","es");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mensajeEspanol = Utils.performPostCall("https://www.bing.com/ttranslatev3", (HashMap) vars);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("xyz", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            traducido = decomposeJson(mensajeEspanol);
            tvBot.setText(traducido);
            mensaje.speak(traducido, TextToSpeech.QUEUE_FLUSH, null);
            botEs = traducido;

            Log.v("xyz", "bthablar botEs: "+botEs+" botEn: "+botEn);
            if(botEs != null && botEn != null){
                initFireBase("bot");
            }
        }
    }

    //para subir informacion al firebase
    //para iniciar firebase tools->firebase
    //cambiar reglas del firebase

    private void initFireBase(String hablante) {
        //singleton
        //solo se crea una instancia de ese objeto gracias al getInstance y siempre es la misma
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //para ver toda la informacion de la BD o que nos diga donde esta el error
        DatabaseReference referenciaItem = database.getReference("item");
        referenciaItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("xyz", "data changed: " + dataSnapshot.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("xyz", "error: " + databaseError.toException());
            }
        });

        //sacamos la fecha y hora
        String fecha = initDay();
        String hora = initHour();

        //texto que queremos meter
        //ChatSentence item = new ChatSentence("bye", "adios", "bot", hora);
        ChatSentence item;
        if(hablante == usuario)
            item = new ChatSentence(usuEn, usuEs, hablante, hora);
        else
            item = new ChatSentence(botEn, botEs, hablante, hora);
        
        Map<String, Object> map = new HashMap<>();
        String key = referenciaItem.child(fecha).push().getKey();
        map.put(usuario+"/"+fecha+"/" + key, item.toMap());
        referenciaItem.updateChildren(map);

        //
        referenciaItem.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v("xyz", "task succesfull");
                } else {
                    Log.v("xyz", task.getException().toString());
                }
            }
        });
    }

    private String initDay(){
        String fecha;
        if(Integer.toString((c2.get(Calendar.MONTH)+1)).length()<2){
            fecha = ""+c2.get(Calendar.YEAR)+"0"+(c2.get(Calendar.MONTH)+1)+""+c2.get(Calendar.DATE);
        }else{
            fecha = ""+c2.get(Calendar.YEAR)+""+(c2.get(Calendar.MONTH)+1)+""+c2.get(Calendar.DATE);
        }
        return fecha;
    }

    private String initHour(){
        String hora = ""+c2.get(Calendar.HOUR_OF_DAY)+":"+c2.get(Calendar.MINUTE)+":"+c2.get(Calendar.SECOND);
        return hora;
    }
}
