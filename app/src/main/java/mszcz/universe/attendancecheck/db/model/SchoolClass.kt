package mszcz.universe.attendancecheck.db.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import io.reactivex.Single
import mszcz.universe.attendancecheck.Utilities.ClassesWithStudents

@Entity(
    tableName = "school_classes",
    indices = arrayOf(
        Index(
            value = ["class_name"],
            unique = true
        )
    )
)

data class SchoolClass(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "class_name") var name: String
)

@Dao
interface ISchoolClasses {
    @Query("Select * from school_classes")
    fun selectAll(): Single<List<SchoolClass>>

    @Insert(onConflict = REPLACE)
    fun addNewClass(schoolClass: SchoolClass): Completable

    @Insert(onConflict = REPLACE)
    fun addNewClass2(schoolClass: SchoolClass): Single<Long>

    @Query("select class_name from school_classes order by id desc limit 1")
    fun lastClass(): Single<String>

    @Query("select sc.class_name as className, s.name as studentName, s.surname as studentSurname from school_classes sc left outer join students s on sc.id = s.class_id")
    fun getAllClassesAndStudents(): Single<List<ClassesWithStudents>>
}
