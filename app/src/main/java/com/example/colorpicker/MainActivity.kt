package com.example.colorpicker
import android.os.Bundle
import android.view.View

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch

import android.text.TextWatcher
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    //color values
    private lateinit var redV: EditText
    private lateinit var greenV: EditText
    private lateinit var blueV: EditText
    private lateinit var colorPreview: View

    //sliders
    private lateinit var redSl: SeekBar
    private lateinit var greenSl: SeekBar
    private lateinit var blueSl: SeekBar

    //switches
    private lateinit var redSw: Switch
    private lateinit var greenSw: Switch
    private lateinit var blueSw: Switch


    //buttons
    private lateinit var resetButton: Button
    private lateinit var copyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redSw = findViewById(R.id.redSwitch)
        greenSw = findViewById(R.id.greenSwitch)
        blueSw = findViewById(R.id.blueSwitch)

        redSw.thumbDrawable.setColorFilter(Color.parseColor("#FF5733"), PorterDuff.Mode.MULTIPLY)
        greenSw.thumbDrawable.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.MULTIPLY)
        blueSw.thumbDrawable.setColorFilter(Color.parseColor("#0000FF"), PorterDuff.Mode.MULTIPLY)

        redSl = findViewById(R.id.redSlider)
        greenSl = findViewById(R.id.greenSlider)
        blueSl = findViewById(R.id.blueSlider)


        redV = findViewById(R.id.redValue)
        greenV = findViewById(R.id.greenValue)
        blueV = findViewById(R.id.blueValue)
        colorPreview = findViewById(R.id.colorPreview)

        resetButton = findViewById(R.id.resetButton)
        resetButton.setBackgroundColor(Color.parseColor("#2f7eed"))

        copyButton = findViewById(R.id.copyColorButton)
        copyButton.setBackgroundColor(Color.parseColor("#2f7eed"))

        // sliders values
        redSl.progress = 0
        greenSl.progress = 0
        blueSl.progress = 0
        redV.setText("0.0")
        greenV.setText("0.0")
        blueV.setText("0.0")

        //  switch listeners
        redSw.setOnCheckedChangeListener { _, isChecked ->
            redSl.isEnabled = isChecked
            redV.isEnabled = isChecked
            if (!isChecked) {
                redSl.progress = 0
                redV.setText("0.0")
            }
            updateColorPreview()
        }

        greenSw.setOnCheckedChangeListener { _, isChecked ->
            greenSl.isEnabled = isChecked
            greenV.isEnabled = isChecked
            if (!isChecked) {
                greenSl.progress = 0
                greenV.setText("0.0")
            }
            updateColorPreview()
        }

        blueSw.setOnCheckedChangeListener { _, isChecked ->
            blueSl.isEnabled = isChecked
            blueV.isEnabled = isChecked
            if (!isChecked) {
                blueSl.progress = 0
                blueV.setText("0.0")
            }
            updateColorPreview()
        }

        // slider listeners
        redSl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                redV.setText("%.2f".format(progress / 100.0))
                updateColorPreview()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        greenSl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                greenV.setText("%.2f".format(progress / 100.0))
                updateColorPreview()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        blueSl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blueV.setText("%.2f".format(progress / 100.0))
                updateColorPreview()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Set up value listeners
        redV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val value = s.toString().toFloatOrNull()
                if (value != null && value >= 0 && value <= 1) {
                    redSl.progress = (value * 100).toInt()
                    updateColorPreview()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        greenV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val value = s.toString().toFloatOrNull()
                if (value != null && value >= 0 && value <= 1) {
                    greenSl.progress = (value * 100).toInt()
                    updateColorPreview()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        blueV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val value = s.toString().toFloatOrNull()
                if (value != null && value >= 0 && value <= 1) {
                    blueSl.progress = (value * 100).toInt()
                    updateColorPreview()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // put rgb reset button listener
        resetButton.setOnClickListener {
            redSl.progress = 0
            greenSl.progress = 0
            blueSl.progress = 0


            redV.setText("0.0")
            greenV.setText("0.0")
            blueV.setText("0.0")


            redSw.isChecked = false
            greenSw.isChecked = false
            blueSw.isChecked = false
            updateColorPreview()
        }
    }

    // function to get the color code in hex format
    //used for copy function later
    private fun getColorCode(): String {
        val redIntensity = if (redSw.isChecked) redV.text.toString().toFloat() else 0f
        val greenIntensity = if (greenSw.isChecked) greenV.text.toString().toFloat() else 0f
        val blueIntensity = if (blueSw.isChecked) blueV.text.toString().toFloat() else 0f

        val red = (redIntensity * 255).toInt()
        val green = (greenIntensity * 255).toInt()
        val blue = (blueIntensity * 255).toInt()

        // Format the color code as a hex string
        return String.format("#%02X%02X%02X", red, green, blue)
    }

    // function to copy text to the clipboard
    private fun copyToClipboard(textToCopy: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Color Code", textToCopy)
        clipboardManager.setPrimaryClip(clipData)

        // Optionally, you can display a toast message to confirm the copy action
        Toast.makeText(this, "Color code copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun updateColorPreview() {
        val redIntensity = if (redSw.isChecked) redV.text.toString().toFloat() else 0f
        val greenIntensity = if (greenSw.isChecked) greenV.text.toString().toFloat() else 0f
        val blueIntensity = if (blueSw.isChecked) blueV.text.toString().toFloat() else 0f
        colorPreview.setBackgroundColor(Color.rgb(
            (redIntensity * 255).toInt(),
            (greenIntensity * 255).toInt(),
            (blueIntensity * 255).toInt()
        ))
    }
}
