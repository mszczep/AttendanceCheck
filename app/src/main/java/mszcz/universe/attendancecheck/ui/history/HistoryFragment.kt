package mszcz.universe.attendancecheck.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import mszcz.universe.attendancecheck.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.OffsetDateTime

class HistoryFragment : Fragment(), HistoryAdapter.HistoryListClickListener {

    private val viewModel: HistoryViewModel by viewModel()
    private var recyclerView: RecyclerView? = null
    private val sectionAdapter = SectionedRecyclerViewAdapter()
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = root.findViewById(R.id.recyclerview_history)
        recyclerViewSetup()

        return root
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun recyclerViewSetup() {

        disposable.add(
            viewModel.getHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ historyWithClasses ->
                    val groupedList = historyWithClasses.groupBy { it.attendanceDate }
                    groupedList.forEach {
                        sectionAdapter.addSection(
                            HistoryAdapter(
                                it.key.toString(),
                                it.value.distinct(),
                                this
                            )
                        )
                    }
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = sectionAdapter
                }, {
                    //TODO Error handling
                })
        )


    }

    override fun onHeaderRootViewClicked(section: HistoryAdapter) {
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

    override fun onItemRootViewClicked(
        section: HistoryAdapter,
        className: String,
        attendanceDate: OffsetDateTime
    ) {
        Toast.makeText(
            requireContext(),
            "You clicked $className with date: $attendanceDate",
            Toast.LENGTH_SHORT
        ).show()
    }

}