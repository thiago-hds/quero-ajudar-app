package com.br.queroajudar.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class API

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CoroutineScopeIO
