package com.caseytmorris.orroifttt.switchcontrol

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.formatRoom

@BindingAdapter("roomControlString")
fun TextView.setRoomControlString(item: RoomControl?) {
    item?.let {
        text = formatRoom(it)
    }
}