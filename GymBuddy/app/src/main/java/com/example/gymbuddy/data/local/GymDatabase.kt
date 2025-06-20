package com.example.gymbuddy.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [User::class, Workout::class, Schedule::class], version = 5, exportSchema = false) // Tingkatkan versi di sini!
abstract class GymDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: GymDatabase? = null

        fun getDatabase(context: Context): GymDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymDatabase::class.java,
                    "gym_database"
                )
                    // .fallbackToDestructiveMigration() tetap pertahankan ini untuk pengembangan
                    .fallbackToDestructiveMigration() // Ini akan menghapus data lama saat skema berubah
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}