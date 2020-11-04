package mszcz.universe.attendancecheck.ui.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import mszcz.universe.attendancecheck.db.AppDatabase
import mszcz.universe.attendancecheck.db.model.SchoolClass
import mszcz.universe.attendancecheck.db.model.Student

class AttendanceViewModel(val db: AppDatabase) : ViewModel() {

    fun selectAllClasses(): Single<List<SchoolClass>> {
        return db.schoolClassesInterface().selectAll()
    }

    fun selectClassStudents(classId: Int): Single<List<Student>> {
        return db.studentsInterface().getAllClassStudents(classId)
    }

}