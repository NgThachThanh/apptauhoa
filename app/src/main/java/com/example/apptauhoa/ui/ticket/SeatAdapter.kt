package com.example.apptauhoa.ui.ticket

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.data.model.Seat
import com.example.apptauhoa.data.model.SeatStatus
import com.example.apptauhoa.databinding.ItemHeaderBinding
import com.example.apptauhoa.databinding.ItemSeatRowBinding
import com.example.apptauhoa.databinding.ItemSleeperCompartmentBinding
import com.example.apptauhoa.databinding.ItemUtilitySpaceBinding
import com.google.android.material.button.MaterialButton

private const val TYPE_HEADER = 0
private const val TYPE_SEAT_ROW = 1
private const val TYPE_SLEEPER_COMPARTMENT = 2
private const val TYPE_UTILITY = 3

class SeatAdapter(
    private val onSeatClicked: (Seat) -> Unit
) : ListAdapter<RailCarDisplayItem, RecyclerView.ViewHolder>(RailCarDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RailCarDisplayItem.Header -> TYPE_HEADER
            is RailCarDisplayItem.SeatRow -> TYPE_SEAT_ROW
            is RailCarDisplayItem.SleeperCompartment -> TYPE_SLEEPER_COMPARTMENT
            is RailCarDisplayItem.UtilitySpace -> TYPE_UTILITY
            else -> throw IllegalArgumentException("Invalid view type at position $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(ItemHeaderBinding.inflate(inflater, parent, false))
            TYPE_SEAT_ROW -> SeatRowViewHolder(ItemSeatRowBinding.inflate(inflater, parent, false), onSeatClicked)
            TYPE_SLEEPER_COMPARTMENT -> SleeperViewHolder(ItemSleeperCompartmentBinding.inflate(inflater, parent, false), onSeatClicked)
            TYPE_UTILITY -> UtilityViewHolder(ItemUtilitySpaceBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RailCarDisplayItem.Header -> (holder as HeaderViewHolder).bind(item)
            is RailCarDisplayItem.SeatRow -> (holder as SeatRowViewHolder).bind(item)
            is RailCarDisplayItem.SleeperCompartment -> (holder as SleeperViewHolder).bind(item)
            is RailCarDisplayItem.UtilitySpace -> (holder as UtilityViewHolder).bind(item)
        }
    }
}

abstract class BaseSeatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    protected fun bindSeatButtonState(button: MaterialButton, seat: Seat?, onSeatClicked: (Seat) -> Unit) {
        if (seat == null) {
            button.visibility = View.INVISIBLE
            return
        }
        button.visibility = View.VISIBLE
        button.text = seat.number
        button.setOnClickListener { onSeatClicked(seat) }

        button.isCheckable = false

        val (backgroundColor, textColor, strokeWidth) = when (seat.status) {
            SeatStatus.AVAILABLE -> Triple(Color.WHITE, Color.BLACK, 2)
            SeatStatus.SELECTED -> Triple(Color.parseColor("#2196F3"), Color.WHITE, 0)
            SeatStatus.BOOKED -> Triple(Color.DKGRAY, Color.WHITE, 0)
        }

        button.backgroundTintList = ColorStateList.valueOf(backgroundColor)
        button.setTextColor(textColor)
        button.strokeWidth = strokeWidth
        if (strokeWidth > 0) {
            button.strokeColor = ColorStateList.valueOf(Color.LTGRAY)
        }
    }
}

class HeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RailCarDisplayItem.Header) {
        binding.title.text = item.title
    }
}

class SeatRowViewHolder(
    private val binding: ItemSeatRowBinding,
    private val onSeatClicked: (Seat) -> Unit
) : BaseSeatViewHolder(binding.root) {
    fun bind(item: RailCarDisplayItem.SeatRow) {
        binding.rowNumber.text = item.seats.firstOrNull()?.rowNumber?.toString() ?: ""
        val seatMap = item.seats.associateBy { it.positionInRow }

        bindSeatButtonState(binding.seatA, seatMap["1"], onSeatClicked)
        bindSeatButtonState(binding.seatB, seatMap["2"], onSeatClicked)
        bindSeatButtonState(binding.seatC, seatMap["3"], onSeatClicked)
        bindSeatButtonState(binding.seatD, seatMap["4"], onSeatClicked)
    }
}

class SleeperViewHolder(
    private val binding: ItemSleeperCompartmentBinding,
    private val onSeatClicked: (Seat) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val bedAdapter = BedAdapter(onSeatClicked)

    init {
        binding.rvBeds.adapter = bedAdapter
    }

    fun bind(item: RailCarDisplayItem.SleeperCompartment) {
        bedAdapter.submitList(item.beds)
        val spanCount = if (item.beds.size > 4) 3 else 2
        binding.rvBeds.layoutManager = GridLayoutManager(itemView.context, spanCount)
    }
}

class UtilityViewHolder(private val binding: ItemUtilitySpaceBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RailCarDisplayItem.UtilitySpace) {
        binding.utilityText.text = item.type
    }
}

class RailCarDiffCallback : DiffUtil.ItemCallback<RailCarDisplayItem>() {
    override fun areItemsTheSame(oldItem: RailCarDisplayItem, newItem: RailCarDisplayItem): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: RailCarDisplayItem, newItem: RailCarDisplayItem): Boolean {
        return oldItem == newItem
    }
}
