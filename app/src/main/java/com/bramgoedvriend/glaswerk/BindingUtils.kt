package com.bramgoedvriend.glaswerk

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Student
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

@BindingAdapter("itemAmountString")
fun TextView.setItemAmountString(item: Item?) {
    item?.let {
        text = item.aantal.toString()
    }
}

@BindingAdapter("itemMinString")
fun TextView.setItemMinString(item: Item?) {
    item?.let {
        text = item.min_aantal.toString()
    }
}

@BindingAdapter("itemMaxString")
fun TextView.setItemMaxString(item: Item?) {
    item?.let {
        text = item.max_aantal.toString()
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

@BindingAdapter("studentNameString")
fun TextView.setStudentNameString(student: Student?) {
    student?.let {
        text = String.format("%s %s", student.voornaam, student.achternaam)
    }
}

@BindingAdapter("studentNumberBrokenString")
fun TextView.setStudentNumberBrokenString(student: Student?) {
    student?.let {
        text = student.leerlingid.toString()
    }
}