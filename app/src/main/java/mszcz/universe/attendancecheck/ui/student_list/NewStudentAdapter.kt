package mszcz.universe.attendancecheck.ui.student_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import mszcz.universe.attendancecheck.R
import mszcz.universe.attendancecheck.db.model.SchoolClass


//class NewStudentAdapter(context: Context, val resource: Int, val list: List<SchoolClass>) :
//    ArrayAdapter<ViewHolder>(
//        context,
//        resource
//    ) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return createViewFromResource(position, convertView, parent)
//    }
//
//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return createViewFromResource(position, convertView, parent)
//    }
//
//    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(
//            resource,
//            parent,
//            false
//        ) as TextView
//        view.text = list[position].name
//        return view
//    }
//}
//
//class ViewHolder(val name: String) {
//    override fun toString(): String {
//        return "ViewHolder: $name"
//    }
//}


class NewStudentAdapter(context: Context, data: List<SchoolClass>) : ArrayAdapter<SchoolClass?>(context, 0, data) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemId(position: Int): Long {
        //It is just an example
        val data: SchoolClass = getItem(position) as SchoolClass
        return data.id.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var itemView = view
        val viewHolder: ViewHolder
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.dropdown_menu_class_selection_item, null)
            // Do some initialization

            //Retrieve the view on the item layout and set the value.
            viewHolder = ViewHolder(itemView)
            itemView!!.setTag(viewHolder)
        } else {
            viewHolder = itemView.tag as ViewHolder
        }

        //Retrieve your object
        val data: SchoolClass = getItem(position) as SchoolClass
        viewHolder.txt.setText(data.name)

        return itemView
    }
}

class ViewHolder(view: View) {

    val txt: TextView =
        view.findViewById<View>(R.id.dropdown_class_textview) as TextView
}