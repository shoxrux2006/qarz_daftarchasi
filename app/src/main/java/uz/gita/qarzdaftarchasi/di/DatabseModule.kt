package uz.gita.qarzdaftarchasi.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.qarzdaftarchasi.domain.room.AppDataBase
import uz.gita.qarzdaftarchasi.domain.room.DebtDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabseModule {
    @Singleton
    @Provides
    fun provideAppDataBase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDataBase::class.java,
        "book.db"
    ).build()


    @Singleton
    @Provides
    fun provideBookDao(db: AppDataBase): DebtDAO = db.debtDAO()
}