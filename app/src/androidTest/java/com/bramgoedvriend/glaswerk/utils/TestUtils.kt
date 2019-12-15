package com.bramgoedvriend.glaswerk.utils

import com.bramgoedvriend.glaswerk.domain.*

val item1 = Item(
    1,
    1,
    "item1",
    10,
    1,
    10,
    5
)

val item2 = Item(
    2,
    1,
    "item2",
    10,
    1,
    10,
    5
)

val item3 = Item(
    3,
    2,
    "item3",
    10,
    1,
    10,
    5
)

val item4 = Item(
    4,
    2,
    "item4",
    10,
    1,
    10,
    5
)

val klas1 = Klas(1, "1A")

val klas2 = Klas(2, "1B")

val lokaal1 = Lokaal(1, "1.001")

val lokaal2 = Lokaal(2, "1.002")

val student1 = Student(1,1,"Jos", "Peeters")

val student2 = Student(2,1,"Bert", "Vermeulen")

val student3 = Student(3,2,"Jan", "Jansens")

val student4 = Student(4,2,"Bob", "De Maes")

val studentItem1 = StudentItem(1,1,1,1)

val studentItem2 = StudentItem(2,1,2,1)

val studentItem3 = StudentItem(3,2,2,0)

val studentItem4 = StudentItem(4,2,3,0)

val studentAndItem1 = StudentAndStudentItem(student1, arrayListOf(studentItem1, studentItem2))

val studentAndItem2 = StudentAndStudentItem(student2, arrayListOf(studentItem3, studentItem4))

val studentAndItem3 = StudentAndStudentItem(student3)

val studentAndItem4 = StudentAndStudentItem(student4)