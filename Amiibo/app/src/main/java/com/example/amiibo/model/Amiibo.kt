package com.example.amiibo.model

data class Amiibo(
    val amiiboSeries: String,
    val character: String,
    val gameSeries: String,
    val head: String,
    val tail: String,
    val name: String,
    val image: String


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Amiibo

        if (head != other.head) return false
        if (tail != other.tail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = head.hashCode()
        result = 31 * result + tail.hashCode()
        return result
    }
}
