package com.caseytmorris.orroifttt.switchcontrol

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caseytmorris.orroifttt.R
import com.caseytmorris.orroifttt.TextItemViewHolder
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.databinding.ListItemRoomControlBinding
import com.caseytmorris.orroifttt.formatRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SwitchControlAdapter : ListAdapter<RoomControl,SwitchControlAdapter.ViewHolder>(SwitchControlDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.room_name)

        fun bind(item: RoomControl) {
            roomName.text = item.roomName
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_room_control, parent, false)
                return ViewHolder(view)
            }
        }
    }


}

class SwitchControlDiffCallback : DiffUtil.ItemCallback<RoomControl>() {
    override fun areItemsTheSame(oldItem: RoomControl, newItem: RoomControl): Boolean {
        return oldItem.roomId == newItem.roomId
    }

    override fun areContentsTheSame(oldItem: RoomControl, newItem: RoomControl): Boolean {
        return oldItem == newItem
    }
}

//class SwitchControlAdapter(val clickListener: SwitchControlListener) : ListAdapter<DataItem,
//        RecyclerView.ViewHolder>(SwitchControlDiffCallback()) {
//
//    private val adapterScope = CoroutineScope(Dispatchers.Default)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is ViewHolder -> {
//                val roomControlItem = getItem(position) as DataItem.RoomControlItem
//                holder.bind(clickListener,roomControlItem.room)
//            }
//        }
//    }
//
//    fun addHeaderAndSubmitList(list: List<RoomControl>?) {
//        adapterScope.launch {
//            val items = when (list) {
//                null -> listOf(DataItem.Header)
//                else -> listOf(DataItem.Header) + list.map { DataItem.RoomControlItem(it) }
//            }
//            withContext(Dispatchers.Main) {
//                submitList(items)
//            }
//        }
//    }
//
//
//
//    class ViewHolder private constructor(val binding: ListItemRoomControlBinding)
//        : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(clickListener: SwitchControlListener, item: RoomControl) {
//            binding.room = item
//            binding.clickListener = clickListener
//            binding.executePendingBindings()
//        }
//
//        companion object {
//            fun from(parent: ViewGroup) : ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ListItemRoomControlBinding.inflate(layoutInflater,parent,false)
//                return ViewHolder(binding)
//            }
//        }
//    }
//}
//
//

//
//class SwitchControlListener(val clickListener: (roomId: Long) -> Unit) {
//    fun onClick(room: RoomControl) = clickListener(room.roomId)
//}
//
//sealed class DataItem {
//    data class RoomControlItem(val room: RoomControl): DataItem() {
//        override val id = room.roomId
//    }
//
//    object Header: DataItem() {
//        override val id = Long.MIN_VALUE
//    }
//
//    abstract val id: Long
//}