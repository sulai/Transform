package de.sulai.transform

import android.app.Activity
import android.content.*
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private val KEY_REGEX: String = "key_regex"
    private val KEY_REPLACE: String = "key_replace"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            performReplace()
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { regexenerator ->
            val split = regexenerator.split("\n")
            edit_regex.setText( split.getOrNull(1), TextView.BufferType.EDITABLE)
            edit_replace.setText( split.getOrNull(4), TextView.BufferType.EDITABLE)
        }

        val pref = getPreferences(Context.MODE_PRIVATE)
        edit_regex.setText(pref.getString(KEY_REGEX, ""), TextView.BufferType.EDITABLE)
        edit_replace.setText(pref.getString(KEY_REPLACE, ""), TextView.BufferType.EDITABLE)

        performReplace()

    }

    fun performReplace() {
        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

        if (text != null && !readonly) {

            val replacement = replace(text.toString())

            val result = Intent()
            result.putExtra(Intent.EXTRA_PROCESS_TEXT, replacement)
            setResult(Activity.RESULT_OK, result)
            finish()

            copyToClipboard(replacement)

        }
    }

    fun replace(original: String): String {
        val regex = Regex(edit_regex.text.toString())
        return original.replace(regex, edit_replace.text.toString().replace("\\n", "\n"))
    }

    fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Transform", text)
        clipboard.primaryClip = clip
    }

    override fun onStop() {
        super.onStop()
        getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_REGEX, edit_regex.text.toString())
            .putString(KEY_REPLACE, edit_replace.text.toString())
            .apply()
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
