package com.srin.snakegame

import android.util.Log
import java.util.*
import kotlin.jvm.Throws

class Snake(cell:Cell) {
    var tag: String = javaClass.simpleName
    var snakeParts: LinkedList<Cell> = LinkedList()
    lateinit var head: Cell
    var partCount:Int = 0

    init {
        head = cell
        snakeParts.add(head)
        partCount+=1
    }


     interface ISnakeGrow{
         fun isGrown(score:Int)
     }
    lateinit var  issnakeGrow:ISnakeGrow
    fun setIGrowListener(isnakegrow:ISnakeGrow){
        this.issnakeGrow = isnakegrow
    }

    @Throws(SnakeBoard.CrashException::class)
    fun move(cell: Cell) {
        Logger.debug(tag, "move request ")
        snakeParts.forEach {
            if (it == cell )
                throw SnakeBoard.CrashException()
        }

        if (cell.type == TYPE.FOOD) {
            snakeParts.addFirst(cell)
            cell.type = TYPE.SNAKE_NODE
            partCount+=1
            issnakeGrow.isGrown(partCount-1)
        } else {
            val tailCell = snakeParts.removeLast()
            Logger.debug(
                tag,
                "move from ${tailCell.row} ${tailCell.col} ->  ${cell.row} ${cell.col}"
            )
            tailCell.type = TYPE.EMPTY
            snakeParts.addFirst(cell)
            cell.type = TYPE.SNAKE_NODE
        }

    }


}