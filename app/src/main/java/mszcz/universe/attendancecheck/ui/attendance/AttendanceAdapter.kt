package mszcz.universe.attendancecheck.ui.attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mszcz.universe.attendancecheck.R
import mszcz.universe.attendancecheck.db.model.Student

class AttendanceAdapter(val list: List<Student>, private val onItemClicked: (Int) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_attendance, parent, false)

        return ViewHolder(view) {
            onItemClicked(it)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.studentName.text = list[position].name
        holder.studentSurname.text = list[position].surname
        holder.studentAttendanceCheckBox
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class ViewHolder(itemView: View, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

    val studentName: TextView = itemView.findViewById(R.id.item_student_name)
    val studentSurname: TextView = itemView.findViewById(R.id.item_student_surname)
    val studentAttendanceCheckBox: CheckBox = itemView.findViewById(R.id.item_attendance_checkbox)

    init {
        studentAttendanceCheckBox.setOnClickListener { onItemClicked(adapterPosition) }
    }
}
