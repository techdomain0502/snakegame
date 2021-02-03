package com.srin.snakegame

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity(), SnakeBoard.onSwipeListener,Snake.ISnakeGrow {
    lateinit var snake: Snake
    lateinit var snakeBoard: SnakeBoard
    lateinit var scoreView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        snakeBoard = findViewById(R.id.board)
        scoreView = findViewById<TextView>(R.id.score)
        snakeBoard.initBoard(20)
        snakeBoard.setSwipeListener(this)
        snake = Snake(snakeBoard.getCell(0, 0))
        snake.setIGrowListener(this)
    }


    enum class Direction {
        LEFT, RIGHT, TOP, BOTTOM, NONE
    }


    override fun onGameLoaded() {
        countDown?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    private fun stopTimer(){
        countDown?.cancel()
        countDown = null
        snakeBoard.stopFoodGeneration()
    }

    private var countDown: CountDownTimer? = object : CountDownTimer(60000, 200) {
        override fun onTick(millisUntilFinished: Long) {
            try {
                when (currentDirection) {
                    Direction.LEFT -> {
                        val nextCell = snakeBoard.nextCell(Direction.LEFT)
                        snake.move(nextCell)
                    }
                    Direction.RIGHT -> {
                        val nextCell = snakeBoard.nextCell(Direction.RIGHT)
                        snake.move(nextCell)
                    }
                    Direction.TOP -> {
                        val nextCell = snakeBoard.nextCell(Direction.TOP)
                        snake.move(nextCell)
                    }
                    Direction.BOTTOM -> {
                        val nextCell = snakeBoard.nextCell(Direction.BOTTOM)
                        snake.move(nextCell)
                    }
                    else -> {//default lets move to right
                        val nextCell = snakeBoard.nextCell(Direction.RIGHT)
                        snake.move(nextCell)
                    }
                }
                snakeBoard.updateBoard()
            }catch (exception:SnakeBoard.CrashException){
                DialogFactory.getLostDialog(this@GameActivity).show()
                stopTimer()
            }
        }

        override fun onFinish() {

        }

    }

    var currentDirection = Direction.NONE
    override fun toLeft(v: View?) {
        try {
            if (currentDirection == Direction.RIGHT) return
            val nextCell = snakeBoard.nextCell(Direction.LEFT)
            currentDirection = Direction.LEFT
            snake.move(nextCell)
            snakeBoard.updateBoard()

        } catch (exception: SnakeBoard.CrashException) {
            exception.printStackTrace();
            DialogFactory.getLostDialog(this@GameActivity).show()
            stopTimer()
        }
    }

    override fun toRight(v: View?) {
        try {
            if (currentDirection == Direction.LEFT) return
            val nextCell = snakeBoard.nextCell(Direction.RIGHT)
            currentDirection = Direction.RIGHT
            snake.move(nextCell)
            snakeBoard.updateBoard()

        } catch (exception: SnakeBoard.CrashException) {
            exception.printStackTrace();
            DialogFactory.getLostDialog(this@GameActivity).show()
            stopTimer()
        }


    }

    override fun toTop(v: View?) {
        try {
            if (currentDirection == Direction.BOTTOM) return
            val nextCell = snakeBoard.nextCell(Direction.TOP)
            currentDirection = Direction.TOP
            snake.move(nextCell)
            snakeBoard.updateBoard()

        } catch (exception: SnakeBoard.CrashException) {
            exception.printStackTrace();
            DialogFactory.getLostDialog(this@GameActivity).show()
            stopTimer()
        }
    }

    override fun toBottom(v: View?) {
        try {
            if (currentDirection == Direction.TOP) return
            val nextCell = snakeBoard.nextCell(Direction.BOTTOM)
            currentDirection = Direction.BOTTOM
            snake.move(nextCell)
            snakeBoard.updateBoard()

        } catch (exception: SnakeBoard.CrashException) {
            exception.printStackTrace();
            DialogFactory.getLostDialog(this@GameActivity).show()
            stopTimer()
        }
    }

    override fun isGrown(score: Int) {
        scoreView.text = score.toString()
    }
}