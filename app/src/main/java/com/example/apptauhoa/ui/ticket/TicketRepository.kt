package com.example.apptauhoa.ui.ticket

import com.example.apptauhoa.data.model.BookedTicket

object TicketRepository {

    private val _bookedTickets = mutableListOf<BookedTicket>()
    val bookedTickets: List<BookedTicket> = _bookedTickets

    fun addTicket(ticket: BookedTicket) {
        _bookedTickets.add(0, ticket) // Add to the top of the list
    }
}