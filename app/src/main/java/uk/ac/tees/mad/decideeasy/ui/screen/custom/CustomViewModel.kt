package uk.ac.tees.mad.decideeasy.ui.screen.custom

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.decideeasy.data.AnswerEntity
import uk.ac.tees.mad.decideeasy.data.Repository
import uk.ac.tees.mad.decideeasy.utils.Constants
import javax.inject.Inject

@HiltViewModel
class CustomViewModel @Inject constructor(
    private val repository: Repository,
    private val auth: FirebaseAuth,
    private val db:FirebaseFirestore
):ViewModel() {

    private val userId = auth.currentUser?.uid ?:""

    private val _answers = MutableStateFlow(listOf<AnswerEntity>())
    val answers:StateFlow<List<AnswerEntity>> get() = _answers

    init {
        viewModelScope.launch {
            repository.getAnswers(userId).collect{
                _answers.value = it
            }
        }
    }

    fun saveAnswer(answer:String, context: Context){
        var entity = AnswerEntity(
            firebaseId = "",
            userId = userId,
            answer = answer
        )
        viewModelScope.launch {
            db.collection(Constants.USER)
                .document(userId)
                .collection(Constants.ANSWERS)
                .add(entity)
                .addOnSuccessListener { docRef->
                    val documentId = docRef.id
                    entity = entity.copy(firebaseId = documentId)
                    docRef.set(entity, SetOptions.merge())
                    viewModelScope.launch {
                        repository.addAnswer(entity)
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(context,"Failed to save, check network",Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun updateAnswer(entity: AnswerEntity){
        db.collection(Constants.USER)
            .document(userId)
            .collection(Constants.ANSWERS)
            .document(entity.firebaseId)
            .update("answer",entity.answer)
            .addOnSuccessListener {
                viewModelScope.launch {
                    repository.updateAnswer(entity)
                }
            }
    }

    fun delete(entity: AnswerEntity){
        db.collection(Constants.USER)
            .document(userId)
            .collection(Constants.ANSWERS)
            .document(entity.firebaseId)
            .delete()
            .addOnSuccessListener {
                viewModelScope.launch {
                    repository.deleteAnswer(entity)
                }
            }
    }
}