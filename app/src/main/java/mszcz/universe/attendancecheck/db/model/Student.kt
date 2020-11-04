package mszcz.universe.attendancecheck.db.model

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import io.reactivex.Single


@Entity(
    tableName = "students",
    foreignKeys = [ForeignKey(
        entity = SchoolClass::class,
        parentColumns = ["id"],
        childColumns = ["class_id"]
    )]
)

data class Student(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "surname") var surname: String,
    @ColumnInfo(name = "class_id") var classId: Int?
)

@Dao
interface IStudents {
    @Query("Select * from students")
    fun getAll(): Single<List<Student>>

    @Insert
    fun addNewStudent(student: Student): Completable

    @Query("select * from students where id = :studentId")
    fun selectStudent(studentId: Int): LiveData<Student>

    @Query("Select * from students where class_id = :classId")
    fun getAllClassStudents(classId: Int): Single<List<Student>>


}

