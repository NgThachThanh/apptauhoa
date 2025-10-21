package com.example.apptauhoa.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

class StationPickerFragment : Fragment() {

    private lateinit var purpose: String
    private lateinit var stationAdapter: StationAdapter

    private val allStations = listOf(
        Station("SG", "Sài Gòn"),
        Station("HN", "Hà Nội"),
        Station("DD", "Dĩ An"),
        Station("BH", "Biên Hòa"),
        Station("NT", "Nha Trang"),
        Station("DN", "Đà Nẵng")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        purpose = requireArguments().getString("purpose") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station_picker, container, false)
        
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        val recyclerView: RecyclerView = view.findViewById(R.id.stations_recycler_view)
        val searchEditText: EditText = view.findViewById(R.id.search_edit_text)
        val emptyView: TextView = view.findViewById(R.id.empty_view)

        stationAdapter = StationAdapter { station ->
            val result = bundleOf("station" to station)
            setFragmentResult(purpose, result)
            findNavController().popBackStack()
        }
        
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = stationAdapter
        stationAdapter.submitList(allStations)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                val filteredList = if (query.isEmpty()) {
                    allStations
                } else {
                    allStations.filter { 
                        it.name.contains(query, ignoreCase = true) || 
                        it.code.contains(query, ignoreCase = true) 
                    }
                }
                stationAdapter.submitList(filteredList)
                emptyView.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
                recyclerView.visibility = if (filteredList.isEmpty()) View.GONE else View.VISIBLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return view
    }
}

class StationAdapter(private val onStationSelected: (Station) -> Unit) : 
    RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    private var stations: List<Station> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_station, parent, false)
        return StationViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(stations[position], onStationSelected)
    }
    
    override fun getItemCount() = stations.size

    fun submitList(newStations: List<Station>) {
        stations = newStations
        notifyDataSetChanged()
    }

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(station: Station, onStationSelected: (Station) -> Unit) {
            val textView = itemView.findViewById<TextView>(R.id.station_name)
            textView.text = "${station.name} (${station.code})"
            itemView.setOnClickListener { onStationSelected(station) }
        }
    }
}