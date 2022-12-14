package nz.ac.uclive.ojc31.seng440assignment2.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nz.ac.uclive.ojc31.seng440assignment2.data.AppDatabase
import nz.ac.uclive.ojc31.seng440assignment2.data.achievements.AchievementDao
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.BirdApi
import nz.ac.uclive.ojc31.seng440assignment2.data.challenges.ChallengeDao
import nz.ac.uclive.ojc31.seng440assignment2.data.entries.EntryDao
import nz.ac.uclive.ojc31.seng440assignment2.data.images.ImageApi
import nz.ac.uclive.ojc31.seng440assignment2.repository.AchievementRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.ChallengeRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.EntryRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.StatisticsRepository
import nz.ac.uclive.ojc31.seng440assignment2.util.Constants.EBIRD_BASE_URL
import nz.ac.uclive.ojc31.seng440assignment2.util.Constants.FLICKR_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBirdRepository(
        birdApi: BirdApi,
        imageApi: ImageApi
    ) = BirdRepository(birdApi, imageApi)

    @Singleton
    @Provides
    fun provideBirdApi(): BirdApi {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttp = OkHttpClient.Builder().addInterceptor(logger)

        return Retrofit.Builder()
            .baseUrl(EBIRD_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())
            .build()
            .create(BirdApi::class.java)
    }

    @Singleton
    @Provides
    fun provideImageApi(): ImageApi {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttp = OkHttpClient.Builder().addInterceptor(logger)

        return Retrofit.Builder()
            .baseUrl(FLICKR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())
            .build()
            .create(ImageApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEntryRepository(entryDao: EntryDao): EntryRepository {
        return EntryRepository(entryDao = entryDao)
    }

    @Singleton
    @Provides
    fun provideChallengeRepository(challengeDao: ChallengeDao): ChallengeRepository {
        return ChallengeRepository(challengeDao = challengeDao)
    }

    @Singleton
    @Provides
    fun provideAchievementRepository(achievementDao: AchievementDao): AchievementRepository {
        return AchievementRepository(achievementDao = achievementDao)
    }

    @Singleton
    @Provides
    fun provideStatisticRepository(entryDao: EntryDao, achievementDao: AchievementDao): StatisticsRepository {
        return StatisticsRepository(entryDao = entryDao, achievementDao = achievementDao)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return AppDatabase.getInstance(app)
    }

    @Singleton
    @Provides
    fun provideEntryDao(appDatabase: AppDatabase): EntryDao {
        return appDatabase.entryDao()
    }

    @Singleton
    @Provides
    fun provideChallengeDao(appDatabase: AppDatabase): ChallengeDao {
        return appDatabase.challengeDao()
    }

    @Singleton
    @Provides
    fun provideAchievementDao(appDatabase: AppDatabase): AchievementDao {
        return appDatabase.achievementDao()
    }

}