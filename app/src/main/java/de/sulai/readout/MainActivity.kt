package de.sulai.readout

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView
import kotlinx.android.synthetic.main.content_main.*
import android.speech.tts.TextToSpeech
import java.util.*


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

        initTextToSpeech()

    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech?.stop()
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
        val textToSay = edit_text.text.toString()
        textToSpeech?.speak(textToSay, TextToSpeech.QUEUE_ADD, null, null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_en -> {
                textToSpeech?.language = Locale.US
            }
            R.id.action_de -> {
                textToSpeech?.language = Locale.GERMAN
            }
            R.id.action_es -> {
                textToSpeech?.language = Locale.forLanguageTag("es")
            }
            else -> super.onOptionsItemSelected(item)
        }
        textToSpeech?.stop()
        startReading()
        return true
    }
}
