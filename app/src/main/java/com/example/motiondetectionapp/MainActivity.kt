package com.example.motiondetectionapp  // Adjust this to your actual package name

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var textViewX: TextView
    private lateinit var textViewY: TextView
    private lateinit var textViewZ: TextView
    private lateinit var buttonToggle: Button
    private var isDetecting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewX = findViewById(R.id.textViewX)
        textViewY = findViewById(R.id.textViewY)
        textViewZ = findViewById(R.id.textViewZ)
        buttonToggle = findViewById(R.id.buttonToggle)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        buttonToggle.setOnClickListener { toggleDetection() }

        if (accelerometer == null) {
            textViewX.setText(R.string.accelerometer_not_available)
            buttonToggle.isEnabled = false
        }
    }

    private fun toggleDetection() {
        isDetecting = !isDetecting
        if (isDetecting) {
            buttonToggle.setText(R.string.stop)
            startDetection()
        } else {
            buttonToggle.setText(R.string.start)
            stopDetection()
        }
    }

    private fun startDetection() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun stopDetection() {
        sensorManager.unregisterListener(this)
        textViewX.setText(getString(R.string.default_acceleration, "X"))
        textViewY.setText(getString(R.string.default_acceleration, "Y"))
        textViewZ.setText(getString(R.string.default_acceleration, "Z"))
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (isDetecting && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            textViewX.text = getString(R.string.x_format, x)
            textViewY.text = getString(R.string.y_format, y)
            textViewZ.text = getString(R.string.z_format, z)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }

    override fun onResume() {
        super.onResume()
        if (isDetecting) {
            startDetection()
        }
    }

    override fun onPause() {
        super.onPause()
        stopDetection()
    }
}