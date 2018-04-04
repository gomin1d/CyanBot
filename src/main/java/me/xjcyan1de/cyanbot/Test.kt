package me.xjcyan1de.cyanbot

fun main(args: Array<String>) {
    val anglediff =  (355 - 5 + 360) % 360
    println("привет $anglediff")
}

fun test(a: Int) = when (a) {
    1 -> "one"
    2 -> "two"
    else -> ""
}