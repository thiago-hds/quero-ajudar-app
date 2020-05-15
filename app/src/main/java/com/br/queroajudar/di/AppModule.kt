package com.br.queroajudar.di

import com.br.queroajudar.network.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Module(includes = [ViewModelModule::class])
class AppModule{

    @Singleton
    @Provides
    fun provideApiService(@API okHttpClient: OkHttpClient, converterFactory: MoshiConverterFactory)
            = provideService(okHttpClient, converterFactory, ApiService::class.java)

    @API
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor,
                            authenticator: TokenAuthenticator): OkHttpClient
            = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .authenticator(authenticator)
            .build()

    @Provides
    fun provideConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.i(message)
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideMoshi(adapterFactory: KotlinJsonAdapterFactory): Moshi
            = Moshi.Builder()
            .add(adapterFactory)
            .build()

    @Provides
    fun provideKotlinJsonAdapterFactory() = KotlinJsonAdapterFactory()

    @CoroutineScopeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    private fun createRetrofit(
        okhttpClient: OkHttpClient,
        converterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()

//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(okHttpClientBuilder.build())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .build()

    }

    private fun <T> provideService(okhttpClient: OkHttpClient,
                                   converterFactory: MoshiConverterFactory, clazz: Class<T>): T {
        return createRetrofit(okhttpClient, converterFactory).create(clazz)
    }
}