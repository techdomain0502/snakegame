package com.srin.snakegame

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TableLayout

class CustomView(private val ctxt: Context, private val attributeSet: AttributeSet): TableLayout(
    ctxt,attributeSet
) {



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        super.onSizeChanged(w, h, oldw, oldh)
        val layoutParams = layoutParams as RelativeLayout.LayoutParams
        val button = Button(ctxt)
        button.layoutParams = layoutParams
        this.addView(button)
        setStretchAllColumns(true);

    }

}