package com.caseytmorris.orroifttt

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.caseytmorris.orroifttt.database.RoomControl
//import com.caseytmorris.orroifttt.database.WebhookApiKey
//
//fun formatWebhookApiKey(webhookApiKey: WebhookApiKey, resources: Resources): String {
//    val sb = StringBuilder()
//    sb.apply {
//        append("Webhook API: ")
//        append("<br>")
//        append(webhookApiKey.apiKey)
//    }
//
////    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
////    } else {
////        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
////    }
//    return sb.toString()
//}

fun formatRoom(room: RoomControl): String {
    val sb = StringBuilder()
    sb.apply {
        append("Rooms to Control")
            append("<br>")
            append("Room: ${room.roomName}")
            append("<br>")
            append("ID: ${room.roomId}")
            append("<br>")
            append("TurnOn: ${room.turnOnWebhook}")


    }

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
//    } else {
//        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
//    }

    return sb.toString()
}

fun formatRoomsList(rooms: List<RoomControl>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append("Rooms to Control")
        append("<br>")
        rooms.forEach {
            append("Room: ${it.roomName}")
            append("<br>")
            append("ID: ${it.roomId}")
            append("<br>")
            append("TurnOn: ${it.turnOnWebhook}")
            append("<br>")
            append("<br>")
        }



    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)