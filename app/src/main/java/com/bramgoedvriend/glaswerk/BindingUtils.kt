package com.bramgoedvriend.glaswerk

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bramgoedvriend.glaswerk.domain.Item
import org.w3c.dom.Text

@BindingAdapter("itemNameString")
fun TextView.setItemNameString(item: Item?) {
    item?.let {
        text = item.naam
    }
}

@BindingAdapter("itemAantalString")
fun TextView.setItemAantalString(item: Item?) {
    item?.let {
        text = item.aantal.toString()
    }
}

@BindingAdapter("itemAantalOfTotalString")
fun TextView.setItemAantalOfTotalString(item: Item?) {
    item?.let {
        text = String.format("%d / %d", item.aantal,item.max_aantal)
    }
}

@BindingAdapter("itemOrderString")
fun TextView.setItemOrderString(item: Item?) {
    item?.let {
        val aantalBestellen = (item.max_aantal - item.aantal) / item.bestel_hoeveelheid
        text = String.format("%d x %d", aantalBestellen, item.bestel_hoeveelheid)
    }
}

@BindingAdapter("itemLokaalString")
fun TextView.setItemLokaalString(item: Item?) {
    item?.let {
        text = item.lokaal_naam
    }
}