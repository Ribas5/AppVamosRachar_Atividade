package com.example.vamosracharribamar;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener,TextToSpeech.OnInitListener{

    EditText editValor, editGrupo;
    TextView tvRes;
    FloatingActionButton share, tocar;
    TextToSpeech ttsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editValor = (EditText) findViewById(R.id.editValor);
        editValor.addTextChangedListener((TextWatcher) this);

        editGrupo = (EditText) findViewById(R.id.editGrupo);
        editGrupo.addTextChangedListener((TextWatcher) this);

        tvRes = (EditText)findViewById(R.id.editResult);
        share = (FloatingActionButton) findViewById(R.id.btShare);
        share.setOnClickListener(this);


        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);

        tocar= (FloatingActionButton) findViewById(R.id.btTocar);
        tocar.setOnClickListener(this);

    }

    private void editValor(Object editText) {
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                // the user has the necessary data - create the TTS
                ttsPlayer = new TextToSpeech(this, this);
            } else {
                // no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent
                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.v("PDM",editValor.getText().toString());

        try {
            double res = Double.parseDouble(editValor.getText().toString());
            res= (res/ Double.parseDouble(editGrupo.getText().toString()));
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            tvRes.setText(nf.format(res) );

        }catch (Exception e){
            tvRes.setText("0,00");
        }


    }

    @Override
    public void onClick(View v) {
        if(v == share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Conta dividida, resultado de "+ tvRes.getText().toString());
            startActivity(intent);
        } if(v==tocar) {
                if (ttsPlayer!=null) {
                    ttsPlayer.speak("O valor por pessoa é de " + tvRes.getText().toString() , TextToSpeech.QUEUE_FLUSH, null, "ID1");
                }
        }

    }


        public void onInit ( int initStatus) {
            //checando a inicialização
            if (initStatus == TextToSpeech.SUCCESS) {
                Toast.makeText(this, "TTS ativado...",
                        Toast.LENGTH_LONG).show();
            } else if (initStatus == TextToSpeech.ERROR) {
                Toast.makeText(this, "Sem TTS habilitado...",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
