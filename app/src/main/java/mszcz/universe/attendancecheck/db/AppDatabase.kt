package mszcz.universe.attendancecheck.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mszcz.universe.attendancecheck.db.model.SchoolClass
import mszcz.universe.attendancecheck.db.model.SchoolClassesInterface
import mszcz.universe.attendancecheck.db.model.Student
import mszcz.universe.attendancecheck.db.model.IStudents

@Database(
    entities = [
        Student::class,
        SchoolClass::class
    ],
    version = 11,
    exportSchema = false
)
//@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentsInterface(): IStudents
    abstract fun schoolClassesInterface(): SchoolClassesInterface

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
