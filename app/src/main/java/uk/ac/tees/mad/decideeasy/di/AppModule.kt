package uk.ac.tees.mad.decideeasy.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.decideeasy.data.AnswerDao
import uk.ac.tees.mad.decideeasy.data.AnswerDatabase
import uk.ac.tees.mad.decideeasy.data.Repository
import uk.ac.tees.mad.decideeasy.data.RepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStore():FirebaseFirestore{
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context):AnswerDatabase{
        return AnswerDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAnswerDao(database: AnswerDatabase):AnswerDao{
        return database.answerDao()
    }

    @Provides
    @Singleton
    fun provideRepository(dao:AnswerDao):Repository{
        return RepositoryImpl(dao)
    }
}