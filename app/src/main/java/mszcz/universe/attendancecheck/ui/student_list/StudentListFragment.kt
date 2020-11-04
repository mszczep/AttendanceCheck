package mszcz.universe.attendancecheck.ui.student_list

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_add_new_class.view.*
import kotlinx.android.synthetic.main.dialog_add_new_student.view.*
import mszcz.universe.attendancecheck.R
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment responsible for displaying classes with students. Sports a sectioned recycler view that hides the sections content on header click.
 */
class StudentListFragment : Fragment(), StudentListAdapter.ClickListener {

    private val viewModel: StudentListViewModel by viewModel()
    private val disposable = CompositeDisposable()
    private val sectionAdapter = SectionedRecyclerViewAdapter()
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_student_list, container, false)
        setHasOptionsMenu(true)

        recyclerView = root.findViewById(R.id.recyclerview_student_list)
        recyclerViewSetup()

        return root
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    /**
     *  Setup recyclerView, grab inital data and load it
     */
    private fun recyclerViewSetup() {
        disposable.add(
            viewModel.getAllClassesAndStudents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ classesWithStudents ->
                    val sortedClasses = classesWithStudents.groupBy { it.className }
                    sortedClasses.forEach {
                        sectionAdapter.addSection(StudentListAdapter(it.key, it.value, this))
                    }
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = sectionAdapter
                }, { error ->
                    //TODO Error handling
                })
        )
    }

    /**
     * Refreshing recyclerView with new data
     */
    private fun refreshRecyclerView() {
        disposable.add(
            viewModel.getAllClassesAndStudents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ classesWithStudents ->
                    sectionAdapter.removeAllSections()
                    val sortedClasses = classesWithStudents.groupBy { it.className }
                    sortedClasses.forEach {
                        sectionAdapter.addSection(StudentListAdapter(it.key, it.value, this))
                    }
                    sectionAdapter.notifyDataSetChanged()
                }, { error ->
                    //TODO Error handling
                })
        )


    }

    /**
     * Adding new class
     */
    private fun addNewClass() {
        val view = layoutInflater.inflate(R.layout.dialog_add_new_class, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .create()
        dialog.show()

        view.ok_button_add_new_class.setOnClickListener {
            val className = view.text_input_class_name.text.toString()

            disposable.add(
                viewModel.addNewClass(className)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(
                            requireContext(),
                            "Completed successfully! Added class: $className",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        refreshRecyclerView()
                    }, { error ->
                        //TODO Error handling
                        Toast.makeText(
                            requireContext(),
                            "Error: ${error.message}.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    })
            )

            dialog.dismiss()
        }

        view.cancel_button_add_new_class.setOnClickListener {
            dialog.dismiss()
        }
    }

    /**
     * Adding new student
     */
    private fun addNewStudent() {

        val view = layoutInflater.inflate(R.layout.dialog_add_new_student, null)
        var selectedItemPosition: Int? = null
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .create()

        disposable.add(
            viewModel.selectAllClasses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ classes ->
                    val adapter = NewStudentAdapter(requireContext(), classes)
                    val classSelection = view.dropdown_menu_class_selection
                    classSelection.setAdapter(adapter)
                    //TODO After selecting an item from the spinner the whole object gets put into the selection i.e. -> User selects class '6e' but the whole object SchoolClass(id = 3, Name = 6e) is displayed instead
                    classSelection.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, position, id ->
                            classSelection.setText(
                                classes[position].name,
                                TextView.BufferType.EDITABLE
                            )
                            selectedItemPosition = position
                        }

                    view.ok_button_student.setOnClickListener {

                        if (view.text_input_student_name.text.isNullOrBlank() || view.text_input_student_surname.text.isNullOrBlank() || selectedItemPosition == null) {
                            Toast.makeText(
                                requireContext(),
                                "Należy uzupełnić wszystkie pola",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }

                        disposable.add(
                            viewModel.addNewStudent(
                                view.text_input_student_name.text.toString(),
                                view.text_input_student_surname.text.toString(),
                                classes[selectedItemPosition!!].id
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(
                                        requireContext(),
                                        "Dodano nowego ucznia",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    refreshRecyclerView()
                                }, {
                                    Toast.makeText(
                                        requireContext(),
                                        "Error: ${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        )


                        dialog.dismiss()
                    }

                    view.cancel_button_student.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()
                }, {
                    //TODO Better error handling
                    Toast.makeText(
                        requireContext(),
                        "Brak klas? : ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_class -> {
                addNewClass()
                true
            }
            R.id.add_new_student -> {
                addNewStudent()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onHeaderRootViewClicked(section: StudentListAdapter) {
        val sectionedAdapter: SectionAdapter = sectionAdapter.getAdapterForSection(section)

        val wasExpanded = section.isExpanded()
        val previousItemsTotal = section.contentItemsTotal

        section.setExpansion(!wasExpanded)
        sectionedAdapter.notifyHeaderChanged()

        if (wasExpanded)
            sectionedAdapter.notifyItemRangeRemoved(0, previousItemsTotal)
        else
            sectionedAdapter.notifyAllItemsInserted()


    }

}