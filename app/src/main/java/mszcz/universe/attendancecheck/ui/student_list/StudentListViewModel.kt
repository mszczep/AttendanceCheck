package mszcz.universe.attendancecheck.ui.student_list


import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Single
import mszcz.universe.attendancecheck.Utilities.ClassesWithStudents
import mszcz.universe.attendancecheck.db.AppDatabase
import mszcz.universe.attendancecheck.db.model.SchoolClass
import mszcz.universe.attendancecheck.db.model.Student

class StudentListViewModel(val db: AppDatabase) : ViewModel() {

    fun addNewClass(className: String): Completable {
        val newClass = SchoolClass(0, className)
        return db.schoolClassesInterface().addNewClass(newClass)
    }

    fun selectAllClasses(): Single<List<SchoolClass>> {
        return db.schoolClassesInterface().selectAll()
    }

    fun selectClass(): Single<String> {
        return db.schoolClassesInterface().lastClass()
    }

    fun addNewStudent(studentName: String, studentSurname: String, studentClassId: Int?): Completable{
        val student = Student(0, studentName, studentSurname, studentClassId)
        return db.studentsInterface().addNewStudent(student)
    }

    fun selectAllStudents(): Single<List<Student>>{
        return db.studentsInterface().getAll()
    }

    fun getAllClassesAndStudents(): Single<List<ClassesWithStudents>> {
        return db.schoolClassesInterface().getAllClassesAndStudents()
    }

}