package com.example.apptauhoa.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.apptauhoa.data.model.BookedTicket
import com.example.apptauhoa.data.model.Coach
import com.example.apptauhoa.data.model.Station
import com.example.apptauhoa.data.model.Trip
import com.example.apptauhoa.data.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "train_ticket.db"
        private const val DATABASE_VERSION = 4 // Increased to 4

        // Table Users
        const val TABLE_USERS = "users"
        const val KEY_USER_ID = "id"
        const val KEY_USER_USERNAME = "username" // NEW
        const val KEY_USER_EMAIL = "email"
        const val KEY_USER_PASSWORD = "password"
        const val KEY_USER_NAME = "full_name"
        const val KEY_USER_ROLE = "role"
        const val KEY_USER_PHONE = "phone"
        const val KEY_USER_DOB = "dob"

        // Table Stations
        const val TABLE_STATIONS = "stations"
        const val KEY_STATION_CODE = "code"
        const val KEY_STATION_NAME = "name"

        // Table Trips
        const val TABLE_TRIPS = "trips"
        const val KEY_TRIP_ID = "id"
        const val KEY_TRIP_CODE = "train_code"
        const val KEY_TRIP_ORIGIN = "origin_code"
        const val KEY_TRIP_DEST = "dest_code"
        const val KEY_TRIP_DEPARTURE = "departure_time"
        const val KEY_TRIP_ARRIVAL = "arrival_time"
        const val KEY_TRIP_DURATION = "duration"
        const val KEY_TRIP_PRICE = "price"
        const val KEY_TRIP_DATE = "trip_date"
        const val KEY_TRIP_CLASS = "class_title"

        // Table Coaches
        const val TABLE_COACHES = "coaches"
        const val KEY_COACH_ID = "id"
        const val KEY_COACH_TRIP_ID = "trip_id"
        const val KEY_COACH_NAME = "name"
        const val KEY_COACH_TYPE = "type"
        const val KEY_COACH_PRICE = "price"
        const val KEY_COACH_TOTAL_SEATS = "total_seats"
        const val KEY_COACH_AVAIL_SEATS = "avail_seats"

        // Table Tickets
        const val TABLE_TICKETS = "tickets"
        const val KEY_TICKET_ID = "id"
        const val KEY_TICKET_USER_ID = "user_id"
        const val KEY_TICKET_TRIP_ID = "trip_id"
        const val KEY_TICKET_SEAT = "seat_number"
        const val KEY_TICKET_CODE = "booking_code"
        const val KEY_TICKET_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Users Table (Added username)
        db.execSQL("CREATE TABLE $TABLE_USERS ("
                + "$KEY_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_USER_USERNAME TEXT UNIQUE," 
                + "$KEY_USER_EMAIL TEXT,"
                + "$KEY_USER_PASSWORD TEXT,"
                + "$KEY_USER_NAME TEXT,"
                + "$KEY_USER_PHONE TEXT,"
                + "$KEY_USER_DOB TEXT,"
                + "$KEY_USER_ROLE TEXT)")

        // Create Stations Table
        db.execSQL("CREATE TABLE $TABLE_STATIONS ("
                + "$KEY_STATION_CODE TEXT PRIMARY KEY,"
                + "$KEY_STATION_NAME TEXT)")

        // Create Trips Table
        db.execSQL("CREATE TABLE $TABLE_TRIPS ("
                + "$KEY_TRIP_ID TEXT PRIMARY KEY,"
                + "$KEY_TRIP_CODE TEXT,"
                + "$KEY_TRIP_ORIGIN TEXT,"
                + "$KEY_TRIP_DEST TEXT,"
                + "$KEY_TRIP_DEPARTURE TEXT,"
                + "$KEY_TRIP_ARRIVAL TEXT,"
                + "$KEY_TRIP_DURATION TEXT,"
                + "$KEY_TRIP_PRICE INTEGER,"
                + "$KEY_TRIP_DATE TEXT,"
                + "$KEY_TRIP_CLASS TEXT)")

        // Create Coaches Table
        db.execSQL("CREATE TABLE $TABLE_COACHES ("
                + "$KEY_COACH_ID TEXT PRIMARY KEY,"
                + "$KEY_COACH_TRIP_ID TEXT,"
                + "$KEY_COACH_NAME TEXT,"
                + "$KEY_COACH_TYPE TEXT,"
                + "$KEY_COACH_PRICE INTEGER,"
                + "$KEY_COACH_TOTAL_SEATS INTEGER,"
                + "$KEY_COACH_AVAIL_SEATS INTEGER)")

        // Create Tickets Table
        db.execSQL("CREATE TABLE $TABLE_TICKETS ("
                + "$KEY_TICKET_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_TICKET_USER_ID INTEGER,"
                + "$KEY_TICKET_TRIP_ID TEXT,"
                + "$KEY_TICKET_SEAT TEXT,"
                + "$KEY_TICKET_CODE TEXT,"
                + "$KEY_TICKET_STATUS TEXT)")

        insertMockData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRIPS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COACHES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TICKETS")
        onCreate(db)
    }

    private fun insertMockData(db: SQLiteDatabase) {
        // Stations
        val stations = listOf(
            Station("SG", "Sài Gòn"),
            Station("HN", "Hà Nội"),
            Station("DD", "Dĩ An"),
            Station("BH", "Biên Hòa"),
            Station("NT", "Nha Trang"),
            Station("DN", "Đà Nẵng"),
            Station("HUE", "Huế"),
            Station("VINH", "Vinh")
        )
        for (station in stations) {
            val values = ContentValues().apply {
                put(KEY_STATION_CODE, station.code)
                put(KEY_STATION_NAME, station.name)
            }
            db.insert(TABLE_STATIONS, null, values)
        }

        // Admin User (Login via Username: admin)
        val admin = ContentValues().apply {
            put(KEY_USER_USERNAME, "admin") // Username login
            put(KEY_USER_EMAIL, "admin@dsvn.vn") // Fake email
            put(KEY_USER_PASSWORD, "admin")
            put(KEY_USER_NAME, "Quản Trị Viên")
            put(KEY_USER_ROLE, "admin")
        }
        db.insert(TABLE_USERS, null, admin)
    }

    // --- TRIPS & COACHES ---

    fun addTrip(trip: Trip): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TRIP_ID, trip.id)
            put(KEY_TRIP_CODE, trip.trainCode)
            put(KEY_TRIP_ORIGIN, trip.originStation)
            put(KEY_TRIP_DEST, trip.destinationStation)
            put(KEY_TRIP_DEPARTURE, trip.departureTime)
            put(KEY_TRIP_ARRIVAL, trip.arrivalTime)
            put(KEY_TRIP_DURATION, trip.duration)
            put(KEY_TRIP_PRICE, trip.price)
            put(KEY_TRIP_DATE, trip.tripDate)
            put(KEY_TRIP_CLASS, trip.classTitle)
        }
        return db.insert(TABLE_TRIPS, null, values)
    }

    fun addCoach(tripId: String, name: String, type: String, price: Long, totalSeats: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_COACH_ID, "${tripId}_${name.replace(" ", "")}")
            put(KEY_COACH_TRIP_ID, tripId)
            put(KEY_COACH_NAME, name)
            put(KEY_COACH_TYPE, type)
            put(KEY_COACH_PRICE, price)
            put(KEY_COACH_TOTAL_SEATS, totalSeats)
            put(KEY_COACH_AVAIL_SEATS, totalSeats)
        }
        db.insert(TABLE_COACHES, null, values)
    }

    fun getCoachesByTripId(tripId: String): List<Coach> {
        val list = ArrayList<Coach>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_COACHES, null, "$KEY_COACH_TRIP_ID=?", arrayOf(tripId), null, null, KEY_COACH_NAME)
        
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getString(it.getColumnIndexOrThrow(KEY_COACH_ID))
                    val name = it.getString(it.getColumnIndexOrThrow(KEY_COACH_NAME))
                    val type = it.getString(it.getColumnIndexOrThrow(KEY_COACH_TYPE))
                    val price = it.getLong(it.getColumnIndexOrThrow(KEY_COACH_PRICE))
                    val total = it.getInt(it.getColumnIndexOrThrow(KEY_COACH_TOTAL_SEATS))
                    val avail = it.getInt(it.getColumnIndexOrThrow(KEY_COACH_AVAIL_SEATS))
                    
                    list.add(Coach(id, name, type, avail, total, price, emptyList()))
                } while (it.moveToNext())
            }
        }
        return list
    }

    fun searchTrips(origin: String, dest: String, date: String): List<Trip> {
        val tripList = ArrayList<Trip>()
        val selectQuery = "SELECT * FROM $TABLE_TRIPS WHERE $KEY_TRIP_ORIGIN = ? AND $KEY_TRIP_DEST = ?"
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, arrayOf(origin, dest))

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_ID))
                    val code = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_CODE))
                    val originName = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_ORIGIN))
                    val destName = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DEST))
                    val depTime = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DEPARTURE))
                    val arrTime = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_ARRIVAL))
                    val duration = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DURATION))
                    val price = it.getLong(it.getColumnIndexOrThrow(KEY_TRIP_PRICE))
                    val tripDate = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DATE))
                    val classTitle = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_CLASS))

                    if (tripDate == date) {
                        tripList.add(Trip(
                            id = id,
                            trainCode = code,
                            classTitle = classTitle,
                            seatsLeft = 50,
                            departureTime = depTime,
                            arrivalTime = arrTime,
                            duration = duration,
                            originStation = originName,
                            destinationStation = destName,
                            price = price,
                            tripDate = tripDate
                        ))
                    }
                } while (it.moveToNext())
            }
        }
        return tripList
    }
    
    fun mockTripsForSearch(origin: String, dest: String, date: String) {
        val tripId = "TRIP_${System.currentTimeMillis()}"
        val basePrice = 550000L
        val trip = Trip(
            id = tripId,
            trainCode = "SE${(1..20).random()}",
            classTitle = "Hỗn hợp (Ngồi/Nằm)",
            seatsLeft = 100,
            departureTime = "08:00",
            arrivalTime = "16:00",
            duration = "8h 00m",
            originStation = origin,
            destinationStation = dest,
            price = basePrice,
            tripDate = date
        )
        addTrip(trip)
        
        addCoach(tripId, "Toa 1", "Ngồi mềm điều hòa", basePrice, 60)
        addCoach(tripId, "Toa 2", "Ngồi mềm điều hòa", basePrice, 60)
        addCoach(tripId, "Toa 3", "Giường nằm khoang 4", (basePrice * 1.3).toLong(), 28)
        addCoach(tripId, "Toa 4", "Giường nằm khoang 4", (basePrice * 1.3).toLong(), 28)
    }

    // --- OTHERS ---

    fun getAllStations(): List<Station> {
        val stationList = ArrayList<Station>()
        val selectQuery = "SELECT * FROM $TABLE_STATIONS"
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val code = it.getString(it.getColumnIndexOrThrow(KEY_STATION_CODE))
                    val name = it.getString(it.getColumnIndexOrThrow(KEY_STATION_NAME))
                    stationList.add(Station(code, name))
                } while (it.moveToNext())
            }
        }
        return stationList
    }

    fun addTicket(ticket: BookedTicket) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TICKET_TRIP_ID, ticket.tripId)
            put(KEY_TICKET_SEAT, ticket.selectedSeatsInfo)
            put(KEY_TICKET_CODE, ticket.bookingCode)
            put(KEY_TICKET_STATUS, ticket.status)
        }
        db.insert(TABLE_TICKETS, null, values)
    }

    fun addUser(name: String, email: String, pass: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USER_NAME, name)
            put(KEY_USER_EMAIL, email) // User uses email as login ID usually
            put(KEY_USER_USERNAME, email) // Default username is email for normal users
            put(KEY_USER_PASSWORD, pass)
            put(KEY_USER_ROLE, "user")
        }
        return db.insert(TABLE_USERS, null, values)
    }

    // UPDATED: Check both Email OR Username
    fun checkUser(input: String, pass: String): User? {
        val db = this.readableDatabase
        // Query checks if input matches EITHER email OR username
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(KEY_USER_ID, KEY_USER_EMAIL, KEY_USER_NAME, KEY_USER_ROLE, KEY_USER_PHONE, KEY_USER_DOB),
            "($KEY_USER_EMAIL=? OR $KEY_USER_USERNAME=?) AND $KEY_USER_PASSWORD=?",
            arrayOf(input, input, pass),
            null, null, null
        )
        var user: User? = null
        cursor?.use {
            if (it.moveToFirst()) {
                user = User(
                    id = it.getInt(it.getColumnIndexOrThrow(KEY_USER_ID)),
                    email = it.getString(it.getColumnIndexOrThrow(KEY_USER_EMAIL)),
                    fullName = it.getString(it.getColumnIndexOrThrow(KEY_USER_NAME)),
                    role = it.getString(it.getColumnIndexOrThrow(KEY_USER_ROLE)),
                    phone = it.getString(it.getColumnIndexOrThrow(KEY_USER_PHONE)) ?: "",
                    dob = it.getString(it.getColumnIndexOrThrow(KEY_USER_DOB)) ?: ""
                )
            }
        }
        return user
    }

    fun getUserById(id: Int): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(KEY_USER_ID, KEY_USER_EMAIL, KEY_USER_NAME, KEY_USER_ROLE, KEY_USER_PHONE, KEY_USER_DOB),
            "$KEY_USER_ID=?",
            arrayOf(id.toString()),
            null, null, null
        )
        var user: User? = null
        cursor?.use {
            if (it.moveToFirst()) {
                user = User(
                    id = it.getInt(it.getColumnIndexOrThrow(KEY_USER_ID)),
                    email = it.getString(it.getColumnIndexOrThrow(KEY_USER_EMAIL)),
                    fullName = it.getString(it.getColumnIndexOrThrow(KEY_USER_NAME)),
                    role = it.getString(it.getColumnIndexOrThrow(KEY_USER_ROLE)),
                    phone = it.getString(it.getColumnIndexOrThrow(KEY_USER_PHONE)) ?: "",
                    dob = it.getString(it.getColumnIndexOrThrow(KEY_USER_DOB)) ?: ""
                )
            }
        }
        return user
    }

    fun updateUser(user: User): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USER_NAME, user.fullName)
            put(KEY_USER_PHONE, user.phone)
            put(KEY_USER_DOB, user.dob)
        }
        return db.update(TABLE_USERS, values, "$KEY_USER_ID=?", arrayOf(user.id.toString()))
    }
}