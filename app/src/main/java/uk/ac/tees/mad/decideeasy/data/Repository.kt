package uk.ac.tees.mad.decideeasy.data

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun addAnswer(entity: AnswerEntity)
    fun getAnswers(userId: String): Flow<List<AnswerEntity>>
    suspend fun updateAnswer(entity: AnswerEntity)
    suspend fun deleteAnswer(entity: AnswerEntity)
}