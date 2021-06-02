package com.Client.Table.ui.preferenceSearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Client.Table.data.model.UsersSearchResult
import com.Client.Table.R


class SearchResultAdapter(
        private val context: Context,
        private val dataset: List<UsersSearchResult>
) : RecyclerView.Adapter<SearchResultAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_search_result, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = item.username
    }

    override fun getItemCount() = dataset.size
}