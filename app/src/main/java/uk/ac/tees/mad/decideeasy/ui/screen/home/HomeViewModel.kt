package uk.ac.tees.mad.decideeasy.ui.screen.home

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.decideeasy.data.AnswerEntity
import uk.ac.tees.mad.decideeasy.data.Repository
import uk.ac.tees.mad.decideeasy.utils.Constants
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val repository: Repository,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
):ViewModel(), SensorEventListener{
    private val sensorManager: SensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val shakeThreshold = 12f

    private val userId = auth.currentUser?.uid ?:""

    private val _answers = MutableStateFlow(listOf<AnswerEntity>())
    val answers:StateFlow<List<AnswerEntity>> get() = _answers

    private val _isShaken = MutableStateFlow(false)
    val isShaken:StateFlow<Boolean> get() = _isShaken

    private var isListening = false

    init {
        viewModelScope.launch {
            repository.getAnswers(userId).collect{
                if (it.isNotEmpty()){
                    _answers.value = it
                }
                else{
                    fetchFromFirebase()
                }
            }
        }
    }

    private fun fetchFromFirebase(){
        db.collection(Constants.USER)
            .document(userId)
            .collection(Constants.ANSWERS)
            .get()
            .addOnSuccessListener { documents->
                val mAnswers =documents.mapNotNull { doc->
                    doc.toObject(AnswerEntity::class.java)
                }
                viewModelScope.launch {
                    mAnswers.forEach {
                        repository.addAnswer(it)
                    }
                }
                _answers.value = mAnswers
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching quotes", e)
            }
    }

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