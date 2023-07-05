package com.xxmrk888ytxx.threads2

sealed class Ship(open val capacity:Int) {

    data class Bread(
        override val capacity: Int,
    ) : Ship(capacity)

    data class Banana(
        override val capacity: Int,
    ) : Ship(capacity)

    data class Clothes(
        override val capacity: Int,
    ) : Ship(capacity)
}