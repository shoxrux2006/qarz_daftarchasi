package uz.gita.banking.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.qarzdaftarchasi.domain.repository.MainRepository
import uz.gita.qarzdaftarchasi.domain.repository.impl.MainRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds
    Singleton]
    fun provideMainRepo(mainRepositoryImpl: MainRepositoryImpl): MainRepository

}