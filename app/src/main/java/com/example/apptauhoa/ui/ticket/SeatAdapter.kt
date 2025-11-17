package com.example.apptauhoa.ui.ticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.databinding.ItemSeatRowBinding
import com.example.apptauhoa.databinding.ItemSleeperCompartmentBinding
import com.example.apptauhoa.databinding.ItemUtilitySpaceBinding
import com.google.android.material.button.MaterialButton

private const val TYPE_SEAT_ROW = 1
private const val TYPE_SLEEPER_COMPARTMENT = 2
private const val TYPE_UTILITY = 3

class SeatAdapter(
    private val onSeatClicked: (Seat) -> Unit
) : ListAdapter<RailCarDisplayItem, RecyclerView.ViewHolder>(RailCarDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RailCarDisplayItem.SeatRow -> TYPE_SEAT_ROW
            is RailCarDisplayItem.SleeperCompartment -> TYPE_SLEEPER_COMPARTMENT
            is RailCarDisplayItem.UtilitySpace -> TYPE_UTILITY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SEAT_ROW -> {
                val binding = ItemSeatRowBinding.inflate(inflater, parent, false)
                SeatRowViewHolder(binding, onSeatClicked)
            }
            TYPE_SLEEPER_COMPARTMENT -> {
                val binding = ItemSleeperCompartmentBinding.inflate(inflater, parent, false)
                SleeperViewHolder(binding, onSeatClicked)
            }
            else -> {
                val binding = ItemUtilitySpaceBinding.inflate(inflater, parent, false)
                UtilityViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RailCarDisplayItem.SeatRow -> (holder as SeatRowViewHolder).bind(item)
            is RailCarDisplayItem.SleeperCompartment -> (holder as SleeperViewHolder).bind(item)
            is RailCarDisplayItem.UtilitySpace -> (holder as UtilityViewHolder).bind(item)
        }
    }
}

// --- VIEW HOLDERS ---

abstract class BaseSeatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    protected fun bindSeatButtonState(button: MaterialButton, seat: Seat?, onSeatClicked: (Seat) -> Unit) {
        if (seat == null) {
            button.visibility = View.INVISIBLE
            return
        }
        button.visibility = View.VISIBLE
        button.text = seat.number
        button.setOnClickListener { onSeatClicked(seat) }

        when (seat.status) {
            SeatStatus.AVAILABLE -> {
                button.isEnabled = true
                button.isChecked = false
            }
            SeatStatus.SELECTED -> {
                button.isEnabled = true
                button.isChecked = true
            }
            SeatStatus.BOOKED, SeatStatus.PENDING -> {
                button.isEnabled = false
                button.isChecked = false
            }
        }
    }
}


class SeatRowViewHolder(
    private val binding: ItemSeatRowBinding,
    private val onSeatClicked: (Seat) -> Unit
) : BaseSeatViewHolder(binding.root) {
    fun bind(item: RailCarDisplayItem.SeatRow) {
        binding.rowNumber.text = item.seats.firstOrNull()?.rowNumber.toString()
        val seatMap: Map<String, Seat> = item.seats.associateBy { it.positionInRow }

        bindSeatButtonState(binding.seatA, seatMap["A"], onSeatClicked)
        bindSeatButtonState(binding.seatB, seatMap["B"], onSeatClicked)
        bindSeatButtonState(binding.seatC, seatMap["C"], onSeatClicked)
        bindSeatButtonState(binding.seatD, seatMap["D"], onSeatClicked)
    }
}

class SleeperViewHolder(
    private val binding: ItemSleeperCompartmentBinding,
    private val onSeatClicked: (Seat) -> Unit
) : BaseSeatViewHolder(binding.root) {
    fun bind(item: RailCarDisplayItem.SleeperCompartment) {
        binding.compartmentLabel.text = "Khoang ${item.beds.firstOrNull()?.compartmentNumber}"
        val bedMap = item.beds.associateBy { it.positionInRow }

        bindSeatButtonState(binding.bedLeft, bedMap["1"], onSeatClicked)
        bindSeatButtonState(binding.bedRight, bedMap["2"], onSeatClicked)
    }
}

class UtilityViewHolder(private val binding: ItemUtilitySpaceBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RailCarDisplayItem.UtilitySpace) {
        binding.utilityText.text = item.type
    }
}


// --- DIFFUTIL ---

class RailCarDiffCallback : DiffUtil.ItemCallback<RailCarDisplayItem>() {
    override fun areItemsTheSame(oldItem: RailCarDisplayItem, newItem: RailCarDisplayItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RailCarDisplayItem, newItem: RailCarDisplayItem): Boolean {
        // Simple equality check is enough since data classes are used
        return oldItem == newItem
    }
}
