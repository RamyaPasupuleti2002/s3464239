package uk.ac.tees.mad.decideeasy.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao:AnswerDao
):Repository {

    override suspend fun addAnswer(entity: AnswerEntity) {
        dao.addAnswer(entity)
    }

    override fun getAnswers(userId: String): Flow<List<AnswerEntity>> {
        return dao.getAnswers(userId)
    }

    override suspend fun updateAnswer(entity: AnswerEntity) {
        dao.updateAnswer(entity)
    }

    override suspend fun deleteAnswer(entity: AnswerEntity) {
        dao.deleteAnswer(entity)
    }
}