package mszcz.universe.attendancecheck.Utilities

import java.time.OffsetDateTime

data class ClassesWithStudents(val className: String, val studentName: String?, val studentSurname: String?)
data class HistoryWithClasses(val className: String, val attendanceDate: OffsetDateTime)