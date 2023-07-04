package com.longhrk.mf.ui.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {
    @Singleton
    @Provides
    fun providesFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun providesFirebaseFireStore() = Firebase.firestore
}