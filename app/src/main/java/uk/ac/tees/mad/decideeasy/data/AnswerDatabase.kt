package uk.ac.tees.mad.decideeasy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AnswerEntity::class], version = 1, exportSchema = false)
abstract class AnswerDatabase:RoomDatabase() {
    abstract fun answerDao():AnswerDao

    companion object{

        @Volatile
        private var INSTANCE:AnswerDatabase? = null

        fun getDatabase(context: Context):AnswerDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnswerDatabase::class.java,
                    "answer_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}