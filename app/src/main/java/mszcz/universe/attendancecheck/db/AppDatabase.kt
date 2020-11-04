package mszcz.universe.attendancecheck.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mszcz.universe.attendancecheck.db.model.*

@Database(
    entities = [
        Student::class,
        SchoolClass::class,
        AttendanceHistory::class
    ],
    version = 11,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentsInterface(): IStudents
    abstract fun schoolClassesInterface(): ISchoolClasses
    abstract fun attendanceHistoryInterface(): IAttandanceHistory

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "attendanceCheck.db")
                .build()
    }
}
