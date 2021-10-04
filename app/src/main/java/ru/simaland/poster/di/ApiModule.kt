package ru.simaland.poster.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.simaland.poster.BuildConfig
import ru.simaland.poster.api.ApiService
import ru.simaland.poster.api.AuthApiService
import ru.simaland.poster.auth.AppAuth
import ru.simaland.poster.util.token
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    companion object {
        private const val PREFS_NAME = "poster_prefs"
    }

    @Provides
    fun providePrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @AuthOkHttpClient
    fun provideAuthOkhttp(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    @Provides
    @Singleton
    @ApiServiceOkHttpClient
    fun provideOkhttp(
        prefs: SharedPreferences,
        logging: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                prefs.token?.let { token ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", token)
                        .build()
                    return@addInterceptor chain.proceed(newRequest)
                }
                chain.proceed(chain.request())
            }
            .build()


    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(@AuthOkHttpClient okhttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okhttp)
            .build()

    @Provides
    @Singleton
    @ApiServiceRetrofit
    fun provideApiServiceRetrofit(@ApiServiceOkHttpClient okhttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okhttp)
            .build()

    @Provides
    @Singleton
    fun provideAuthApiService(@AuthRetrofit retrofit: Retrofit): AuthApiService = retrofit.create()

    @Provides
    @Singleton
    fun provideApiService(@ApiServiceRetrofit retrofit: Retrofit): ApiService = retrofit.create()

    @Provides
    @Singleton
    fun provideAuth(prefs: SharedPreferences): AppAuth = AppAuth(prefs)

}

@Qualifier
annotation class AuthOkHttpClient

@Qualifier
annotation class ApiServiceOkHttpClient

@Qualifier
annotation class AuthRetrofit

@Qualifier
annotation class ApiServiceRetrofit
