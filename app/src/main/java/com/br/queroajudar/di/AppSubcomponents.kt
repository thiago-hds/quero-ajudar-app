package com.br.queroajudar.di

import dagger.Module

@Module(subcomponents = [SplashComponent::class, HomeComponent::class, AuthenticationComponent::class])
class AppSubcomponents