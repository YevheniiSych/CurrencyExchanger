package com.currencyexchanger.di

import android.app.Application
import androidx.room.Room
import com.currencyexchanger.BuildConfig
import com.currencyexchanger.data.remote.api.ExchangeRatesApi
import com.currencyexchanger.data.repository.ExchangeRateRepository
import com.currencyexchanger.data.repository.createExchangeRateRepository
import com.currencyexchanger.data.room.ExchangerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExchangerDatabase(app: Application): ExchangerDatabase {
        return Room.databaseBuilder(
            app, ExchangerDatabase::class.java, ExchangerDatabase.DATABASE_NAME
        ).createFromAsset("database/exchanger.db").build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideExchangeRateApi(retrofit: Retrofit): ExchangeRatesApi {
        return retrofit.create(ExchangeRatesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExchangeRateRepository(
        api: ExchangeRatesApi, db: ExchangerDatabase
    ): ExchangeRateRepository {
        return createExchangeRateRepository(api, db)
    }
}