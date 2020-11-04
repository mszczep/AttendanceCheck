package mszcz.universe.attendancecheck.db.model

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import java.time.OffsetDateTime


@Entity(
    tableName = "attendance_history",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["student_id"]
        ),
        ForeignKey(
            entity = SchoolClass::class,
            parentColumns = ["id"],
            childColumns = ["class_id"]
        )]
)

data class AttendanceHistory(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "student_id") var studentId: Int,
    @ColumnInfo(name = "class_id") var classId: Int,
    @ColumnInfo(name = "create_date") var createDate: OffsetDateTime
)

@Dao
interface IAttandanceHistory {
    @Query("Select * from attendance_history")
    fun getAll(): Single<List<AttendanceHistory>>

    @Insert
    fun insertSingleHistory(attendanceHistory: AttendanceHistory): Completable

    @Insert
    fun insertHistory( attendanceHistory: List<AttendanceHistory>): Completable

}