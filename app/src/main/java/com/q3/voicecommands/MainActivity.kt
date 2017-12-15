package com.q3.voicecommands

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecognitionListener, TextToSpeech.OnInitListener {
    private val RESULT_SPEECH = 1001
    lateinit var speech: SpeechRecognizer
    lateinit var tts: TextToSpeech
    lateinit var recognizerIntent:Intent
    lateinit private var list:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list= ArrayList()

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US”=")

        initSpeechRecognizer()
        setTTSListener();

        button.setOnClickListener {
            if(recognizerIntent!=null) {

                speech.startListening(recognizerIntent)
                displayText.setText("listening...")
                btnStop.visibility= View.GONE;
            }
        }

        btnStop.setOnClickListener {
          /*  speech.stopListening()
            displayText.setText("")
            btnStop.visibility=View.VISIBLE
            performAction(list)*/

        }
    }

    override fun onInit(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_SPEECH -> {
                if (resultCode == AppCompatActivity.RESULT_OK && null != data) {
                    val text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    Log.d("Main activity", Arrays.toString(text.toArray()))
                    displayText.setText(text.get(0));
                }
            }
        }
    }

    override fun onReadyForSpeech(bundle: Bundle) {

    }

    override fun onBeginningOfSpeech() {

    }

    override fun onRmsChanged(v: Float) {

    }

    override fun onBufferReceived(bytes: ByteArray) {


    }

    override fun onEndOfSpeech() {
        displayText.setText("")
        progressBar.visibility=View.VISIBLE;
    }

    override fun onError(i: Int) {
        var message = ""
        displayText.setText("")

        progressBar.visibility=View.GONE;

        when (i) {

            SpeechRecognizer.ERROR_AUDIO ->

                message = getString(R.string.error_audio_error)

            SpeechRecognizer.ERROR_CLIENT ->

                message = getString(R.string.error_client)

            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->

                message = getString(R.string.error_permission)

            SpeechRecognizer.ERROR_NETWORK ->

                message = getString(R.string.error_network)

            SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->

                message = getString(R.string.error_timeout)

            SpeechRecognizer.ERROR_NO_MATCH ->

                message = getString(R.string.error_no_match)

            SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->

                message = getString(R.string.error_busy)

            SpeechRecognizer.ERROR_SERVER ->

                message = getString(R.string.error_server)

            SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->

                message = getString(R.string.error_timeout)

            else ->

                message = getString(R.string.error_understand)
        }
        Log.d("Main activity", message)
    }

    override fun onResults(bundle: Bundle) {
        val matches = bundle
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        displayText.setText(matches.get(0));
        Log.d("Main activity", Arrays.toString(matches!!.toArray()))
       list.addAll(matches)
        //displayText.setText("")
        performAction(matches)
    }

    override fun onPartialResults(bundle: Bundle) {

    }

    override fun onEvent(i: Int, bundle: Bundle) {

    }

    fun performAction(list:ArrayList<String>){
        progressBar.visibility=View.GONE;
        for(str in list){


            if(str.contains("mail")){

                val intent = Intent(Intent.ACTION_VIEW)
                val data = Uri.parse("mailto:?subject=''&body=''")
                intent.data = data
                startActivity(intent)
                break
            }
            if(str.contains("hello")){

                speakOut("Greet")
                break
            }
        }
    }


    fun setTTSListener(){
        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                tts!!.language = Locale.US
            } else {
                Log.e("TTS", "Initilization Failed!")
            }
        })
    }


    fun speakOut(str:String){
        when (str) {
            "Greet" -> {
                if(tts!=null){
                    tts.speak("Hello Guesst , How are you!", TextToSpeech.QUEUE_ADD, null)

                }
            }
        }
    }

    fun initSpeechRecognizer(){

        speech = SpeechRecognizer.createSpeechRecognizer(this)
        speech.setRecognitionListener(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,

                "en")

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,

                this.packageName)

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,

                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
    }


}
