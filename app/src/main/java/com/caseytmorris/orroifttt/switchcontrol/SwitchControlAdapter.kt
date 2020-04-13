package com.caseytmorris.orroifttt.switchcontrol

import android.util.Log
import android.view.LayoutInflater
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

class SwitchControlAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<RoomControl>()
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.i("Casey","Adapter Data changed")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view,parent,false) as TextView
        return TextItemViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = formatRoom(item)

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
//class SwitchControlDiffCallback : DiffUtil.ItemCallback<DataItem>() {
//    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
//        return oldItem == newItem
//    }
//}
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