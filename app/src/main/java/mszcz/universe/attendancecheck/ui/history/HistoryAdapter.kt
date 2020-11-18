package mszcz.universe.attendancecheck.ui.history

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import mszcz.universe.attendancecheck.R
import mszcz.universe.attendancecheck.Utilities.HistoryWithClasses
import java.time.OffsetDateTime

class HistoryAdapter(val header: String, val list: List<HistoryWithClasses>, private val clickListener: HistoryListClickListener): Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.section_item_history)
        .headerResourceId(R.layout.section_header)
        .build()
) {
    interface HistoryListClickListener{
        fun onHeaderRootViewClicked(section: HistoryAdapter)
        fun onItemRootViewClicked(section: HistoryAdapter, className: String, attendanceDate: OffsetDateTime)
    }

    var expanded = true

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val itemHolder = holder as HistoryItemViewHolder
        itemHolder.historyClass.text = list[position].className

        itemHolder.rootView.setOnClickListener { clickListener.onItemRootViewClicked(this, list[position].className, list[position].attendanceDate) }
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val headerHolder = holder as HistoryHeaderViewHolder
        headerHolder.sectionHeader.text = header
        headerHolder.rootView.setOnClickListener { clickListener.onHeaderRootViewClicked(this) }
    }

    override fun getContentItemsTotal(): Int {
        return if(expanded) list.size else 0
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return HistoryItemViewHolder(view)
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HistoryHeaderViewHolder(view)
    }

    fun isExpanded(): Boolean {
        return expanded
    }

    fun setExpansion(expansion: Boolean){
        expanded = expansion
    }
}

class HistoryItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val historyClass: TextView = itemView.findViewById(R.id.section_history_class)
    val rootView: View = itemView
}

class HistoryHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val rootView: View = itemView
    val sectionHeader: TextView = itemView.findViewById(R.id.section_header_text_view)
}