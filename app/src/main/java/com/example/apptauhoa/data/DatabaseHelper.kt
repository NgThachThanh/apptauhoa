package com.example.apptauhoa.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.apptauhoa.data.model.BookedTicket
import com.example.apptauhoa.data.model.Coach
import com.example.apptauhoa.data.model.Promotion
import com.example.apptauhoa.data.model.Seat
import com.example.apptauhoa.data.model.Station
import com.example.apptauhoa.data.model.Trip
import com.example.apptauhoa.data.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "train_ticket.db"
        private const val DATABASE_VERSION = 9 // Incremented version for schema change

        // Promotions Table...
        const val TABLE_PROMOTIONS = "promotions"
        const val KEY_PROMO_ID = "id"
        const val KEY_PROMO_CODE = "code"
        const val KEY_PROMO_DESC = "description"
        const val KEY_PROMO_DISCOUNT = "discount_percent"
        const val KEY_PROMO_IMAGE = "image_res_id"

        // Users Table...
        const val TABLE_USERS = "users"
        const val KEY_USER_ID = "id"
        const val KEY_USER_USERNAME = "username"
        const val KEY_USER_EMAIL = "email"
        const val KEY_USER_PASSWORD = "password"
        const val KEY_USER_NAME = "full_name"
        const val KEY_USER_ROLE = "role"
        const val KEY_USER_PHONE = "phone"
        const val KEY_USER_DOB = "dob"

        // Stations Table...
        const val TABLE_STATIONS = "stations"
        const val KEY_STATION_CODE = "code"
        const val KEY_STATION_NAME = "name"

        // Trips Table...
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

        // Coaches Table...
        const val TABLE_COACHES = "coaches"
        const val KEY_COACH_ID = "id"
        const val KEY_COACH_TRIP_ID = "trip_id"
        const val KEY_COACH_NAME = "name"
        const val KEY_COACH_TYPE = "type"
        const val KEY_COACH_PRICE = "price"
        const val KEY_COACH_TOTAL_SEATS = "total_seats"
        const val KEY_COACH_AVAIL_SEATS = "avail_seats"

        // Tickets Table...
        const val TABLE_TICKETS = "tickets"
        const val KEY_TICKET_ID = "id"
        const val KEY_TICKET_USER_ID = "user_id"
        const val KEY_TICKET_TRIP_ID = "trip_id"
        const val KEY_TICKET_SEAT = "seat_number"
        const val KEY_TICKET_CODE = "booking_code"
        const val KEY_TICKET_PRICE = "price"
        const val KEY_TICKET_STATUS = "status"
        const val KEY_TICKET_DEPARTURE_TIME = "departure_timestamp"
        const val KEY_TICKET_ARRIVAL_TIME = "arrival_timestamp"

        // Booked Seats Table (Schema Updated)
        const val TABLE_BOOKED_SEATS = "booked_seats"
        const val KEY_BOOKING_ID = "id"
        const val KEY_BOOKING_TRIP_ID = "trip_id"
        const val KEY_BOOKING_SEAT_ID = "seat_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create tables...
        db.execSQL("CREATE TABLE $TABLE_PROMOTIONS ("
                + "$KEY_PROMO_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_PROMO_CODE TEXT UNIQUE," 
                + "$KEY_PROMO_DESC TEXT,"
                + "$KEY_PROMO_IMAGE TEXT,"
                + "$KEY_PROMO_DISCOUNT INTEGER)")

        db.execSQL("CREATE TABLE $TABLE_USERS ("
                + "$KEY_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_USER_USERNAME TEXT UNIQUE," 
                + "$KEY_USER_EMAIL TEXT,"
                + "$KEY_USER_PASSWORD TEXT,"
                + "$KEY_USER_NAME TEXT,"
                + "$KEY_USER_PHONE TEXT,"
                + "$KEY_USER_DOB TEXT,"
                + "$KEY_USER_ROLE TEXT)")

        db.execSQL("CREATE TABLE $TABLE_STATIONS ("
                + "$KEY_STATION_CODE TEXT PRIMARY KEY,"
                + "$KEY_STATION_NAME TEXT)")

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

        db.execSQL("CREATE TABLE $TABLE_COACHES ("
                + "$KEY_COACH_ID TEXT PRIMARY KEY,"
                + "$KEY_COACH_TRIP_ID TEXT,"
                + "$KEY_COACH_NAME TEXT,"
                + "$KEY_COACH_TYPE TEXT,"
                + "$KEY_COACH_PRICE INTEGER,"
                + "$KEY_COACH_TOTAL_SEATS INTEGER,"
                + "$KEY_COACH_AVAIL_SEATS INTEGER)")

        db.execSQL("CREATE TABLE $TABLE_TICKETS ("
                + "$KEY_TICKET_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_TICKET_USER_ID INTEGER,"
                + "$KEY_TICKET_TRIP_ID TEXT,"
                + "$KEY_TICKET_SEAT TEXT,"
                + "$KEY_TICKET_CODE TEXT,"
                + "$KEY_TICKET_PRICE INTEGER,"
                + "$KEY_TICKET_DEPARTURE_TIME INTEGER,"
                + "$KEY_TICKET_ARRIVAL_TIME INTEGER,"
                + "$KEY_TICKET_STATUS TEXT)")
        
        db.execSQL("CREATE TABLE $TABLE_BOOKED_SEATS ("
                + "$KEY_BOOKING_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_BOOKING_TRIP_ID TEXT NOT NULL,"
                + "$KEY_BOOKING_SEAT_ID TEXT NOT NULL,"
                + "UNIQUE ($KEY_BOOKING_TRIP_ID, $KEY_BOOKING_SEAT_ID))")

        insertMockData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROMOTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRIPS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COACHES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TICKETS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKED_SEATS") // Drop new table
        onCreate(db)
    }

    // ... (rest of the code is the same for now)

    fun getBookedSeatsForTrip(tripId: String): Set<String> {
        val bookedSeatIds = mutableSetOf<String>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKED_SEATS,
            arrayOf(KEY_BOOKING_SEAT_ID),
            "$KEY_BOOKING_TRIP_ID = ?",
            arrayOf(tripId),
            null, null, null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val seatId = it.getString(it.getColumnIndexOrThrow(KEY_BOOKING_SEAT_ID))
                    bookedSeatIds.add(seatId)
                } while (it.moveToNext())
            }
        }
        return bookedSeatIds
    }

    fun addBookedSeats(tripId: String, seats: List<Seat>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            seats.forEach { seat ->
                val values = ContentValues().apply {
                    put(KEY_BOOKING_TRIP_ID, tripId)
                    put(KEY_BOOKING_SEAT_ID, seat.id)
                }
                db.insertWithOnConflict(TABLE_BOOKED_SEATS, null, values, SQLiteDatabase.CONFLICT_IGNORE)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
    
    fun addTicket(ticket: BookedTicket, userId: Int, seats: List<Seat>) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TICKET_USER_ID, userId)
            put(KEY_TICKET_TRIP_ID, ticket.tripId)
            put(KEY_TICKET_SEAT, ticket.selectedSeatsInfo)
            put(KEY_TICKET_CODE, ticket.bookingCode)
            put(KEY_TICKET_STATUS, ticket.status)
            put(KEY_TICKET_PRICE, ticket.originalPrice)
            put(KEY_TICKET_DEPARTURE_TIME, ticket.departureTime)
            put(KEY_TICKET_ARRIVAL_TIME, ticket.arrivalTime)
        }
        val ticketId = db.insert(TABLE_TICKETS, null, values)

        if (ticketId != -1L) {
            addBookedSeats(ticket.tripId, seats)
        }
    }
    
    private fun insertMockData(db: SQLiteDatabase) {
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

        val admin = ContentValues().apply {
            put(KEY_USER_USERNAME, "admin")
            put(KEY_USER_EMAIL, "admin@dsvn.vn")
            put(KEY_USER_PASSWORD, "admin")
            put(KEY_USER_NAME, "Quản Trị Viên")
            put(KEY_USER_ROLE, "admin")
        }
        db.insert(TABLE_USERS, null, admin)
        
        val promotions = listOf(
            Promotion(code="TET2025", description="Vé tàu Tết 2025", discountPercent=10, imageResId="@drawable/promo_tet"),
            Promotion(code="HE2024", description="Chào hè sôi động", discountPercent=20, imageResId="@drawable/promo_summer"),
            Promotion(code="SINHVIEN30", description="Giảm giá sinh viên", discountPercent=30, imageResId="@drawable/promo_student"),
            Promotion(code="NHOM4", description="Đi nhóm 4 người", discountPercent=15, imageResId="@drawable/promo_group")
        )
        for (promo in promotions) {
            val values = ContentValues().apply {
                put(KEY_PROMO_CODE, promo.code)
                put(KEY_PROMO_DESC, promo.description)
                put(KEY_PROMO_DISCOUNT, promo.discountPercent)
                put(KEY_PROMO_IMAGE, promo.imageResId)
            }
            db.insert(TABLE_PROMOTIONS, null, values)
        }
    }
    
    fun getPromotionByCode(code: String): Promotion? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_PROMOTIONS, null, "$KEY_PROMO_CODE=?", arrayOf(code), null, null, null)
        var promo: Promotion? = null
        cursor?.use {
            if (it.moveToFirst()) {
                promo = Promotion(
                    id = it.getInt(it.getColumnIndexOrThrow(KEY_PROMO_ID)),
                    code = it.getString(it.getColumnIndexOrThrow(KEY_PROMO_CODE)),
                    description = it.getString(it.getColumnIndexOrThrow(KEY_PROMO_DESC)),
                    discountPercent = it.getInt(it.getColumnIndexOrThrow(KEY_PROMO_DISCOUNT)),
                    imageResId = it.getString(it.getColumnIndexOrThrow(KEY_PROMO_IMAGE))
                )
            }
        }
        return promo
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
        val allBookedSeatsForTrip = getBookedSeatsForTrip(tripId)

        val cursor = db.query(TABLE_COACHES, null, "$KEY_COACH_TRIP_ID = ?", arrayOf(tripId), null, null, KEY_COACH_NAME)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getString(it.getColumnIndexOrThrow(KEY_COACH_ID))
                    val name = it.getString(it.getColumnIndexOrThrow(KEY_COACH_NAME))
                    val type = it.getString(it.getColumnIndexOrThrow(KEY_COACH_TYPE))
                    val price = it.getLong(it.getColumnIndexOrThrow(KEY_COACH_PRICE))
                    val total = it.getInt(it.getColumnIndexOrThrow(KEY_COACH_TOTAL_SEATS))

                    val bookedSeatsForThisCoach = allBookedSeatsForTrip.count { seatId -> seatId.startsWith(id) }
                    val avail = total - bookedSeatsForThisCoach

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
    fun getTicketsForUser(userId: Int, status: String): List<BookedTicket> {
        val ticketList = ArrayList<BookedTicket>()
        val selectQuery = "SELECT t.*, tr.$KEY_TRIP_CODE, tr.$KEY_TRIP_ORIGIN, tr.$KEY_TRIP_DEST, tr.$KEY_TRIP_DATE FROM $TABLE_TICKETS t JOIN $TABLE_TRIPS tr ON t.$KEY_TICKET_TRIP_ID = tr.$KEY_TRIP_ID WHERE t.$KEY_TICKET_USER_ID = ? AND t.$KEY_TICKET_STATUS = ?"

        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, arrayOf(userId.toString(), status))

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val ticket = BookedTicket(
                        tripId = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_TRIP_ID)),
                        selectedSeatsInfo = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_SEAT)),
                        bookingCode = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_CODE)),
                        status = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_STATUS)),
                        trainCode = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_CODE)),
                        originStation = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_ORIGIN)),
                        destinationStation = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DEST)),
                        departureTime = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_DEPARTURE_TIME)), // Corrected
                        arrivalTime = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_ARRIVAL_TIME)),       // Corrected
                        tripDate = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DATE)),
                        originalPrice = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_PRICE))
                    )
                    ticketList.add(ticket)
                } while (it.moveToNext())
            }
        }
        return ticketList
    }

    fun getTicketByBookingCode(bookingCode: String): BookedTicket? {
        var ticket: BookedTicket? = null
        val selectQuery = "SELECT t.*, tr.$KEY_TRIP_CODE, tr.$KEY_TRIP_ORIGIN, tr.$KEY_TRIP_DEST, tr.$KEY_TRIP_DATE FROM $TABLE_TICKETS t JOIN $TABLE_TRIPS tr ON t.$KEY_TICKET_TRIP_ID = tr.$KEY_TRIP_ID WHERE t.$KEY_TICKET_CODE = ?"
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, arrayOf(bookingCode))
        cursor?.use {
            if (it.moveToFirst()) {
                ticket = BookedTicket(
                    tripId = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_TRIP_ID)),
                    selectedSeatsInfo = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_SEAT)),
                    bookingCode = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_CODE)),
                    status = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_STATUS)),
                    trainCode = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_CODE)),
                    originStation = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_ORIGIN)),
                    destinationStation = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DEST)),
                    departureTime = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_DEPARTURE_TIME)),
                    arrivalTime = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_ARRIVAL_TIME)),
                    tripDate = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DATE)),
                    originalPrice = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_PRICE))
                )
            }
        }
        return ticket
    }

    fun getTicketsByPassengerName(passengerName: String): List<BookedTicket> {
        val ticketList = ArrayList<BookedTicket>()
        val selectQuery = """
            SELECT t.*, tr.$KEY_TRIP_CODE, tr.$KEY_TRIP_ORIGIN, tr.$KEY_TRIP_DEST, tr.$KEY_TRIP_DATE, u.$KEY_USER_NAME
            FROM $TABLE_TICKETS t
            JOIN $TABLE_USERS u ON t.$KEY_TICKET_USER_ID = u.$KEY_USER_ID
            JOIN $TABLE_TRIPS tr ON t.$KEY_TICKET_TRIP_ID = tr.$KEY_TRIP_ID
            WHERE u.$KEY_USER_NAME LIKE ?
        """
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, arrayOf("%$passengerName%"))
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val ticket = BookedTicket(
                        tripId = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_TRIP_ID)),
                        selectedSeatsInfo = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_SEAT)),
                        bookingCode = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_CODE)),
                        status = it.getString(it.getColumnIndexOrThrow(KEY_TICKET_STATUS)),
                        trainCode = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_CODE)),
                        originStation = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_ORIGIN)),
                        destinationStation = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DEST)),
                        departureTime = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_DEPARTURE_TIME)),
                        arrivalTime = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_ARRIVAL_TIME)),
                        tripDate = it.getString(it.getColumnIndexOrThrow(KEY_TRIP_DATE)),
                        originalPrice = it.getLong(it.getColumnIndexOrThrow(KEY_TICKET_PRICE))
                    )
                    ticketList.add(ticket)
                } while (it.moveToNext())
            }
        }
        return ticketList
    }

    fun updateTicketStatus(bookingCode: String, newStatus: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TICKET_STATUS, newStatus)
        return db.update(TABLE_TICKETS, values, "$KEY_TICKET_CODE = ?", arrayOf(bookingCode))
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

    fun checkUser(input: String, pass: String): User? {
        val db = this.readableDatabase
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
}
