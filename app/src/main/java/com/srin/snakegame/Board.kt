package com.srin.snakegame

import android.util.Log
import java.lang.Exception

class Board(val max_row: Int, val max_col: Int, var cells: Array<Array<Cell>>) {


    var row:Int = 0
    var col:Int = 0

    fun getBoardCells():Array<Array<Cell>>{
        return cells
    }

    @Throws (CrashException::class)
    fun nextCell(dir: GameActivity.Direction):Cell{
        if(dir == GameActivity.Direction.BOTTOM){
            row++
        }
        if(dir == GameActivity.Direction.TOP){
           row--;
        }
        if(dir == GameActivity.Direction.LEFT){
           col--;
        }
        if(dir == GameActivity.Direction.RIGHT){
            col++;
        }
        if (inBounds(row,col))
        return cells!![row][col]
       else
            throw CrashException()
    }

    class CrashException:Exception("Snaked Crashed"){

    }

    fun inBounds(row:Int, col:Int):Boolean{
        if(row in 0 until max_row && col in 0 until max_col)
            return true
        return false
    }

    fun generateFood(){
         val row = Math.floor(Math.random()*max_row).toInt()
        val col = Math.floor(Math.random()*max_col).toInt()
        if(cells[row][col].type == TYPE.SNAKE_NODE) return
        cells[row][col].type = TYPE.FOOD
    }

    fun printBoard() {
        cells!!.forEach {
            it.forEach {
            }
            println()
        }
    }
}