package mszcz.universe.attendancecheck.koin

import androidx.room.Room
import mszcz.universe.attendancecheck.db.AppDatabase
import mszcz.universe.attendancecheck.db.model.IStudents
import mszcz.universe.attendancecheck.ui.attendance.AttendanceViewModel
import mszcz.universe.attendancecheck.ui.history.HistoryViewModel
import mszcz.universe.attendancecheck.ui.student_list.StudentListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val appModule = module {
    single { Room.databaseBuilder(androidApplication(), AppDatabase::class.java,"attendanceDB").build() }
    single { get<AppDatabase>().schoolClassesInterface()}
    single { get<AppDatabase>().studentsInterface()}

    viewModel { StudentListViewModel(get()) }
    viewModel { AttendanceViewModel(get())}
    viewModel { HistoryViewModel(get())}
}