package com.example.apptauhoa.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import java.text.Normalizer
import java.util.regex.Pattern

class NotificationsFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var notificationsRecyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var notificationsAdapter: NotificationsAdapter

    // SỬA LỖI: Đây là danh sách gốc, không bao giờ thay đổi (chỉ đọc)
    private val originalNotifications = listOf(
        "Thông báo : Tàu SE1 khởi hành đúng giờ.",
        "Thông báo : Tàu SE2 dự kiến trễ 15 phút.",
        "Thông báo : Có sự thay đổi về lịch trình tàu TN5.",
        "Khuyến mãi: Giảm 20% giá vé cho các chuyến tàu đêm.",
        "Thông báo : Tàu SE8 đã đến ga cuối cùng an toàn.",
        "Cảnh báo: Thời tiết xấu có thể ảnh hưởng đến các chuyến tàu phía Bắc.",
        "Thông báo : Mở bán vé tàu Tết 2026."
    )

    // SỬA LỖI: Đây là danh sách sẽ được hiển thị và có thể thay đổi (thêm, xóa)
    private val displayList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        searchView = view.findViewById(R.id.search_view)
        notificationsRecyclerView = view.findViewById(R.id.notifications_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)

        // SỬA LỖI: Đặt lại danh sách hiển thị về trạng thái ban đầu mỗi khi tạo view
        displayList.clear()
        displayList.addAll(originalNotifications)

        setupRecyclerView()
        setupSearchView()

        // SỬA LỖI: Không cần gọi updateData ở đây nữa vì Adapter đã có dữ liệu khi khởi tạo
        updateViews(displayList.isNotEmpty())

        return view
    }

    private fun setupRecyclerView() {
        notificationsRecyclerView.layoutManager = LinearLayoutManager(context)
        // SỬA LỖI: Khởi tạo Adapter với danh sách hiển thị (displayList)
        notificationsAdapter = NotificationsAdapter(displayList) { position ->
            // Đây là callback được gọi khi một item bị xóa trong adapter
            // Cập nhật lại trạng thái của view
            if (displayList.isEmpty()) {
                updateViews(false)
            }
        }
        notificationsRecyclerView.adapter = notificationsAdapter
        setupItemTouchHelper()
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // SỬA LỖI: Khi vuốt xóa, chúng ta cũng cần xóa item đó khỏi danh sách gốc
                // để nó không xuất hiện lại khi tìm kiếm.
                val itemToRemove = displayList[position]
                originalNotifications.toMutableList().remove(itemToRemove) // Xóa khỏi bản sao của danh sách gốc

                notificationsAdapter.removeItem(position)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(notificationsRecyclerView)
    }

    private fun setupSearchView() {
        searchView.queryHint = "Tìm kiếm thông báo..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })
    }

    private fun filter(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            originalNotifications
        } else {
            val normalizedQuery = query.unaccent().lowercase()
            originalNotifications.filter {
                it.unaccent().lowercase().contains(normalizedQuery)
            }
        }
        // SỬA LỖI: Gọi hàm updateData của Adapter để cập nhật lại giao diện
        notificationsAdapter.updateData(filteredList)
        updateViews(filteredList.isNotEmpty())
    }

    private fun String.unaccent(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(temp).replaceAll("")
    }

    private fun updateViews(hasData: Boolean) {
        notificationsRecyclerView.visibility = if (hasData) View.VISIBLE else View.GONE
        emptyView.visibility = if (hasData) View.GONE else View.VISIBLE
    }

    // SỬA LỖI: Cập nhật lại Adapter
    class NotificationsAdapter(
        private val notifications: MutableList<String>,
        private val onItemRemoved: (Int) -> Unit // Callback để thông báo cho Fragment
    ) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val notificationText: TextView = view.findViewById(R.id.text_notification_content)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.notificationText.text = notifications[position]
        }

        override fun getItemCount() = notifications.size

        fun removeItem(position: Int) {
            if (position in 0 until notifications.size) {
                notifications.removeAt(position)
                notifyItemRemoved(position)
                onItemRemoved(position) // Gọi callback
            }
        }

        fun updateData(newData: List<String>) {
            notifications.clear()
            notifications.addAll(newData)
            notifyDataSetChanged()
        }
    }
}
