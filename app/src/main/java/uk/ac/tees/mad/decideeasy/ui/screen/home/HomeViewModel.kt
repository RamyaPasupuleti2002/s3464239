package uk.ac.tees.mad.decideeasy.ui.screen.home

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application
):ViewModel(), SensorEventListener{
    private val sensorManager: SensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val shakeThreshold = 12f

    private val _isShaken = MutableStateFlow(false)
    val isShaken:StateFlow<Boolean> get() = _isShaken

    private var isListening = false

    fun startListening() {
        resetShakeEvent()
        if (!isListening) {
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
            isListening = true
        }
    }

    fun stopListening() {
        if (isListening) {
            sensorManager.unregisterListener(this)
            isListening = false
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER){
                val (x,y,z) = it.values
                val acceleration = sqrt(x*x + y*y + z*z) - SensorManager.GRAVITY_EARTH
                if (acceleration>shakeThreshold){
                    _isShaken.value = true
                    stopListening()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }

    fun resetShakeEvent() {
        _isShaken.value = false
    }

}