package com.br.queroajudar.di

import dagger.Module

@Module(subcomponents = [SplashComponent::class, MainComponent::class, AuthenticationComponent::class])
class AppSubcomponents