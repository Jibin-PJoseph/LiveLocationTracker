package com.jibin.livelocationtracker.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jibin.livelocationtracker.data.location.GeocoderHelper
import com.jibin.livelocationtracker.data.repository.LocationRepositoryImpl
import com.jibin.livelocationtracker.domain.repository.LocationRepository
import com.jibin.livelocationtracker.domain.usecases.GetAddressUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object AppModule {

    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideLocationRepository(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationRepository {
        return LocationRepositoryImpl(fusedLocationProviderClient, context)
    }
    @Provides
    @Singleton
    fun provideGeocoderHelper(@ApplicationContext context: Context): GeocoderHelper {
        return GeocoderHelper(context)
    }
    @Provides
    @Singleton
    fun provideGetAddressUseCase(repository: LocationRepository): GetAddressUseCase {
        return GetAddressUseCase(repository)
    }
}