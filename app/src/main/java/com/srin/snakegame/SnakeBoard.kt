package com.srin.snakegame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Exception
import kotlin.math.abs
import kotlin.math.floor


class SnakeBoard(private val ctxt: Context, private val attributeSet: AttributeSet) : TableLayout(
    ctxt,
    attributeSet
) {
    private var boardWidth = 0;
    private var noOfrows = 0
    private var noOfcols = 0
    private var buttonDimen = 0
    private var boardSize = 0
    lateinit var rowArray: Array<TableRow>
    private var gDetector: GestureDetector? = null
    private var tag: String = javaClass.simpleName

    val gesture_listener = object : GestureDetector.OnGestureListener {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onShowPress(e: MotionEvent?) {
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                swipeListener.toLeft(null)
                return true
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                swipeListener.toRight(null)
                return true
            }
            if (e1.y - e2.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                swipeListener.toTop(null)
                return true
            } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                swipeListener.toBottom(null)
                return true
            }
            return false
        }

    }

    private lateinit var swipeListener: onSwipeListener
    fun setSwipeListener(listener: onSwipeListener) {
        this.swipeListener = listener
    }


    init {
        setWillNotDraw(false)
        clipChildren = false
        gDetector = GestureDetector(context, gesture_listener)
    }


    interface onSwipeListener {
        fun toLeft(v: View?)
        fun toRight(v: View?)
        fun toTop(v: View?)
        fun toBottom(v: View?)
        fun onGameLoaded()
    }


    companion object {
        private const val SWIPE_MIN_DISTANCE = 60
        private const val SWIPE_THRESHOLD_VELOCITY = 100
    }


    override fun dispatchTouchEvent(me: MotionEvent?): Boolean {
        super.dispatchTouchEvent(me)
        return gDetector!!.onTouchEvent(me)
    }


    private fun initDimens(size: Int) {
        boardSize = size
        noOfrows = size
        noOfcols = size
    }

    fun initBoard(size: Int) {
        initDimens(size)
        cells = Array(noOfrows) { row ->
            Array(noOfcols) { col ->
                Cell(row, col, TYPE.EMPTY)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initialize()
        val handler = Handler(Looper.getMainLooper())
        handler.post { requestLayout() }
    }

    private fun initialize() {
        Logger.debug(tag, "initialize ")

        rowArray = Array(noOfrows) {
            TableRow(ctxt)
        }
        val rowParams = TableRow.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val cellParams = TableRow.LayoutParams(buttonDimen, buttonDimen)

        for (i in 0 until noOfrows) {
            rowArray[i].layoutParams = rowParams
            addView(rowArray[i])
            for (j in 0 until noOfcols) {
                val view = View(ctxt)
                view.background = resources.getDrawable(R.drawable.border)
                view.layoutParams = cellParams
                rowArray[i].addView(view)
            }
        }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Logger.debug(tag, "onMeasure ")
        boardWidth = this.measuredWidth - paddingLeft - paddingRight
        buttonDimen = boardWidth / noOfcols
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        params.height = buttonDimen * noOfrows
        layoutParams = params
    }

    var gameStarted = false
    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        if (!gameStarted) {
            gameStarted = true
            generateFood()
            startGame()
            updateBoard()
        }
    }

    var row: Int = 0
    var col: Int = 0
    private lateinit var cells: Array<Array<Cell>>
    private var prevFruitRow = 0
    private var prevFruitCol = 0

    fun generateFood() {
        if (cells[prevFruitRow][prevFruitCol].type == TYPE.FOOD)
            cells[prevFruitRow][prevFruitCol].type = TYPE.EMPTY

        Logger.debug(tag, "generateFood ")
        val row = Math.floor(Math.random() * noOfrows).toInt()
        val col = Math.floor(Math.random() * noOfcols).toInt()
        if (cells[row][col].type == TYPE.SNAKE_NODE) return
         cells[row][col].type = TYPE.FOOD
         prevFruitCol = col
         prevFruitRow = row
         updateBoard()
         handle.postDelayed({ generateFood() }, 3000)
    }

    private val handle: Handler = Handler(Looper.getMainLooper())

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopFoodGeneration()
    }

     fun stopFoodGeneration() {
        handle.removeCallbacksAndMessages(null)
    }

    fun inBounds(row: Int, col: Int): Boolean {
        if (row in 0 until noOfrows && col in 0 until noOfcols)
            return true
        return false
    }

    fun getCell(startX: Int, startY: Int): Cell {
        return cells!![startX][startY]
    }

    @Throws(CrashException::class)
    fun nextCell(dir: GameActivity.Direction): Cell {
        Logger.debug(tag, "next cell request ")
        if (dir == GameActivity.Direction.BOTTOM) {
            row++
        }
        if (dir == GameActivity.Direction.TOP) {
            row--;
        }
        if (dir == GameActivity.Direction.LEFT) {
            col--;
        }
        if (dir == GameActivity.Direction.RIGHT) {
            col++;
        }
        if (inBounds(row, col))
            return cells!![row][col]
        else
            throw CrashException()
    }

    class CrashException : Exception("Snaked Crashed") {

    }


    fun startGame() {
        Logger.debug(tag, "startgame ")
        val r1 = getChildAt(0) as TableRow
        val cell = r1.getChildAt(0)
        cells[0][0].type = TYPE.SNAKE_NODE
        row = 0
        col = 0
        cell.setBackgroundColor(Color.GREEN);
        swipeListener.onGameLoaded()
    }

    fun getStartX(): Int {
        Logger.debug(tag, "getStartX ")
        return row
    }

    fun getStartY(): Int {
        Logger.debug(tag, "getStartY ")
        return col
    }

    fun updateBoard() {
        Logger.debug(tag, "updateBoard called")
        cells.forEach {
            it.forEach {
                val row = getChildAt(it.row) as TableRow
                val tableCell = row.getChildAt(it.col)
                if (it.type == TYPE.SNAKE_NODE) {
                    tableCell.setBackgroundColor(Color.GREEN)
                } else if (it.type == TYPE.EMPTY) {
                    tableCell.setBackgroundColor(Color.WHITE)
                    tableCell.background = resources.getDrawable(R.drawable.border)
                } else if (it.type == TYPE.FOOD) {
                    tableCell.setBackgroundColor(Color.RED)
                }
            }

        }
    }


}