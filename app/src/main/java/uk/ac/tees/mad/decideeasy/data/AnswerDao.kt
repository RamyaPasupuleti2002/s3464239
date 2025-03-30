package uk.ac.tees.mad.decideeasy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnswer(entity: AnswerEntity)

    @Query("SELECT * FROM answer_table WHERE userId = :userId")
    fun getAnswers(userId: String): Flow<List<AnswerEntity>>

    @Update
    suspend fun updateAnswer(entity: AnswerEntity)

    @Delete
    suspend fun deleteAnswer(entity: AnswerEntity)
}