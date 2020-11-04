package mszcz.universe.attendancecheck.ui.student_list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import mszcz.universe.attendancecheck.R
import mszcz.universe.attendancecheck.Utilities.ClassesWithStudents

class StudentListAdapter(val header: String, val list: List<ClassesWithStudents>, val clickListener: ClickListener): Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.section_item_student_list)
        .headerResourceId(R.layout.section_header_student_list)
        .build()

) {

    interface ClickListener {
        fun onHeaderRootViewClicked(section: StudentListAdapter)
    }

    var expanded: Boolean = true

    override fun onBindItemViewHolder(holder: ViewHolder?, position: Int) {
        val itemHolder = holder as ItemViewHolder

        itemHolder.studentName.text = list[position].studentName
        itemHolder.studentSurname.text = list[position].studentSurname

    }


    override fun onBindHeaderViewHolder(holder: ViewHolder) {
        val headerHolder = holder as HeaderViewHolder
        headerHolder.sectionHeader.text = header
        headerHolder.rootView.setOnClickListener{ v -> clickListener.onHeaderRootViewClicked(this)}
    }

    override fun getContentItemsTotal(): Int {
        return if(expanded) list.size else 0
    }

    override fun getItemViewHolder(view: View): ViewHolder {
        return ItemViewHolder(view)
    }

    override fun getHeaderViewHolder(view: View): ViewHolder {
        return HeaderViewHolder(view)
    }

    fun isExpanded(): Boolean {
        return expanded
    }

    fun setExpansion(expansion: Boolean) {
        expanded = expansion
    }

}

class ItemViewHolder(itemView: View): ViewHolder(itemView){
    val studentName: TextView = itemView.findViewById(R.id.section_student_name)
    val studentSurname: TextView = itemView.findViewById(R.id.section_student_surname)

}

class HeaderViewHolder(itemView: View): ViewHolder(itemView){
    val rootView: View = itemView
    val sectionHeader: TextView = itemView.findViewById(R.id.section_header_student_list)
}