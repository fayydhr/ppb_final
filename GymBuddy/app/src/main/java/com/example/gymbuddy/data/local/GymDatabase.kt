// app/src/main/java/com/example/gymbuddy/data/local/GymDatabase.kt
package com.example.gymbuddy.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [User::class, Workout::class, Schedule::class], version = 6, exportSchema = false) // Pastikan version telah di-increment jika ada perubahan skema
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
                    .fallbackToDestructiveMigration() // Ini akan menghapus data lama setiap kali versi diubah
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}