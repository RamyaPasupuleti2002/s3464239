package uk.ac.tees.mad.decideeasy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer_table")
data class AnswerEntity(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val firebaseId:String,
    val userId:String,
    val answer:String
)
