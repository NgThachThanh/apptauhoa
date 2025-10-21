package com.example.apptauhoa.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

class NotificationsFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var notificationsRecyclerView: RecyclerView
    private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        searchView = view.findViewById(R.id.search_view)
        notificationsRecyclerView = view.findViewById(R.id.notifications_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)

        notificationsRecyclerView.layoutManager = LinearLayoutManager(context)
        notificationsRecyclerView.adapter = NotificationsAdapter(emptyList())

        // Set initial visibility
        updateViews(false)

        return view
    }

    private fun updateViews(hasData: Boolean) {
        notificationsRecyclerView.visibility = if (hasData) View.VISIBLE else View.GONE
        emptyView.visibility = if (hasData) View.GONE else View.VISIBLE
    }

    // A simple empty adapter for now
    class NotificationsAdapter(private val notifications: List<String>) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            // Add your TextViews or other views here
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Inflate your item layout here
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Bind your data here
        }

        override fun getItemCount() = notifications.size
    }
}