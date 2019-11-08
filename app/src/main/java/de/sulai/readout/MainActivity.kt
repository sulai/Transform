package de.sulai.readout

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView
import kotlinx.android.synthetic.main.content_main.*
import android.speech.tts.TextToSpeech
import java.util.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    private var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            if(textToSpeech?.isSpeaking==true)
                textToSpeech?.stop()
            else
                startReading()
        }

        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { text ->
            edit_text.setText( text, TextView.BufferType.EDITABLE)
        }
        intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)?.let { text ->
            edit_text.setText( text, TextView.BufferType.EDITABLE)
        }

        button_de.setOnClickListener {
            textToSpeech?.language = Locale.GERMAN
            startReading()
        }
        button_en.setOnClickListener {
            textToSpeech?.language = Locale.US
            startReading()
        }
        button_es.setOnClickListener {
            textToSpeech?.language = Locale.forLanguageTag("es")
            startReading()
        }

        initTextToSpeech()

    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech?.shutdown()
    }

    private fun initTextToSpeech() {
        textToSpeech = TextToSpeech(this,
            TextToSpeech.OnInitListener { status ->
                if (status == TextToSpeech.SUCCESS) {
                    startReading()
                }
            })
    }

    private fun startReading() {
        textToSpeech?.stop()
        val wholeText = edit_text.text.toString()
        for (chunk in wholeText.split("WAIT:")) {
            val matcher = Pattern.compile("^(\\d+)(.+)").matcher(chunk)
            if (matcher.find()) {
                val time = matcher.group(1).toLong()*1000
                val text = matcher.group(2)
                textToSpeech?.playSilentUtterance(time, TextToSpeech.QUEUE_ADD, null)
                textToSpeech?.speak(text, TextToSpeech.QUEUE_ADD, null, null)
            } else {
                textToSpeech?.speak(chunk, TextToSpeech.QUEUE_ADD, null, null)
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

}
