package mszcz.universe.attendancecheck.ui.attendance

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_attendance.*
import mszcz.universe.attendancecheck.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttendanceFragment : Fragment(), View.OnClickListener {

    private val viewModel: AttendanceViewModel by viewModel()
    private val disposable = CompositeDisposable()
    private var mMenu: Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_attendance, container, false)

        val btn = root.findViewById<Button>(R.id.button_check_attendance)
        btn.setOnClickListener(this)


        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.attendance_menu, menu)
        mMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.attendance_menu_confirm -> {
                if(mMenu != null) mMenu!!.setGroupVisible(R.id.attendance_menu_group, false)
                confirmAttendance()
                true
            }
            R.id.attendance_menu_cancel -> {
                if(mMenu != null) mMenu!!.setGroupVisible(R.id.attendance_menu_group, false)
                cancelAttendance()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button_check_attendance -> {
                selectClass()
            }
        }
    }


    // get attendance -> input into history
    private fun selectClass() {
        val items = arrayListOf<String>()

        if(mMenu != null) mMenu!!.setGroupVisible(R.id.attendance_menu_group, true)

        disposable.add(
            viewModel.selectAllClasses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.forEach { items.add(it.name) }
                    val classes = items.toTypedArray()
                    val dialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Proszę wybrać klasę")
                        .setItems(classes) { _, which ->
                            checkAttendance(it[which].id, it[which].name)
                        }
                        .create()
                    dialog.show()
                }, { error ->
                    //TODO error handling
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                })
        )
    }

    private fun checkAttendance(classId: Int, className: String) {

        val attendingStudents = ArrayList<Int>()

        //TODO Add progressBar
        class_name_text_view.text = className
        button_check_attendance.visibility = View.GONE
        constraint_layout_check_attendance.visibility = View.VISIBLE

        disposable.add(
            viewModel.selectClassStudents(classId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ studentList ->

                    val recyclerView = recyclerview_check_attendance
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    val adapter = AttendanceAdapter(studentList) { position ->

                        val studentId = studentList[position].id

                        if (attendingStudents.contains(studentId))
                            attendingStudents.remove(studentId)
                        else
                            attendingStudents.add(studentId)

                        Toast.makeText(
                            requireContext(),
                            "Attending students: " + attendingStudents,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    recyclerView.adapter = adapter


                }, { error ->
                    //TODO Error handling
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                        .show()

                })
        )
    }

    /**
     * Finish taking attendance/confirm selection.
     */
    private fun confirmAttendance(){

    }

    /**
     * Cancel the attendance process.
     */
    private fun cancelAttendance(){
        button_check_attendance.visibility = View.VISIBLE
        constraint_layout_check_attendance.visibility = View.GONE
        recyclerview_check_attendance.adapter = null
    }

}