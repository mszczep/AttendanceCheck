package mszcz.universe.attendancecheck.ui.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Single
import mszcz.universe.attendancecheck.db.AppDatabase
import mszcz.universe.attendancecheck.db.model.AttendanceHistory
import mszcz.universe.attendancecheck.db.model.SchoolClass
import mszcz.universe.attendancecheck.db.model.Student
import java.time.OffsetDateTime

class AttendanceViewModel(val db: AppDatabase) : ViewModel() {


    //A list holding ids of attending students
    val attendingStudents = ArrayList<Int>()

    /**
     * Returns a list of all school classes
     */
    fun selectAllClasses(): Single<List<SchoolClass>> {
        return db.schoolClassesInterface().selectAll()
    }

    /**
     * Returns a list of all students in a specified school class
     * @param classId Id of a given school class
     */
    fun selectClassStudents(classId: Int): Single<List<Student>> {
        return db.studentsInterface().getAllClassStudents(classId)
    }

    /**
     * Operations on the attending students list
     * @param studentId
     */
    fun studentAttendance(studentId: Int) {
        if (attendingStudents.contains(studentId))
            attendingStudents.remove(studentId)
        else
            attendingStudents.add(studentId)
    }

    /**
     * Formatting a list of school classes for display in a dialog.
     * @param classes List of school classes
     */
    fun formatClasses(classes: List<SchoolClass>): Array<String> {
        val formattedClasses = arrayListOf<String>()
        classes.forEach {
            formattedClasses.add(it.name)
        }
        return formattedClasses.toTypedArray()
    }

    fun insertAttendanceHistory(classId: Int): Completable {
        val list = arrayListOf<AttendanceHistory>()
        attendingStudents.forEach { studentId ->
            list.add(AttendanceHistory(0, studentId, classId, OffsetDateTime.now()))
        }

        return db.attendanceHistoryInterface().insertHistory(list)
    }


    fun testFunDisplayHistory(): Single<List<AttendanceHistory>>{
        return db.attendanceHistoryInterface().getAll()
    }

}