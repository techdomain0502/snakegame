package com.srin.snakegame

enum class TYPE {
    FOOD,
    SNAKE_NODE,
    EMPTY
}
data class Cell (var row:Int, var col:Int, var type:TYPE)