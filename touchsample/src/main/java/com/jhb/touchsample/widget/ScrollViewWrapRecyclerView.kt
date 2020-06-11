package com.jhb.touchsample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView


/**
 * @author jianghongbo
 * @version 1.0
 * @file AutoMeasureViewPager.java
 * @brief
 * @date 2020/6/10
 * All rights reserved.
 */
class ScrollViewWrapRecyclerView @JvmOverloads constructor(
    ctx: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(ctx, attr, defStyleAttr) {

    var headerContent: View? = null
    var canScrollChild: View? = null//这里暂时用的RecyclerView，考虑使用NestedScrollChild接口

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (dy > 0 && currentScrollY < headerContent?.measuredHeight!!) {//优先让ScrollView消费上拉操作
            val atMostDy = headerContent?.measuredHeight!! - currentScrollY
            if (dy > atMostDy) {
                consumed[0] = dx
                consumed[1] = atMostDy
                scrollBy(dx, atMostDy)
            } else {
                consumed[0] = dx
                consumed[1] = dy
                scrollBy(dx, dy)
            }
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return if (currentScrollY == 0)//判断是否到达顶部，如果到达了才通过nestedScrolling机制和外部进行交互，保证事件的传递消费不超过2个以上控件。否则不好控制
            super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
        else
            false
    }

 /*   var startY: Float? = 0f
    var startX: Float? = 0f
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev?.x
                startY = ev?.y
                return super.dispatchTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                if (startY != null && startX != null) {
                    val dy = ev?.y?.minus(startY!!)
                    val dx = ev?.x?.minus(startX!!)
                    if (dy != null && dx != null && dy > dx) {
                        //交由recyclerView去处理
                        return if (canScrollChild is View) {
                            canScrollChild!!.dispatchTouchEvent(ev)
                        } else {
                            super.dispatchTouchEvent(ev)
                        }
                    } else {
                        return super.dispatchTouchEvent(ev)
                    }
                } else {
                    return super.dispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP -> {
                return super.dispatchTouchEvent(ev)
            }
        }
        return super.dispatchTouchEvent(ev)
    }*/

    /* override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
         if (headerContent != null &&  canScrollChild != null) {
             //把事件交给NestedChild类消费
             return canScrollChild!!.dispatchNestedPreFling(velocityX, velocityY)
         }
         return super.dispatchNestedPreFling(velocityX, velocityY)
     }

     override fun dispatchNestedFling(
         velocityX: Float,
         velocityY: Float,
         consumed: Boolean
     ): Boolean {
         println("dispatchNestedFling velocityY:${velocityY}   consumed:${consumed}")
         if (headerContent != null  && canScrollChild != null) {
             //把事件交给NestedChild类消费
             return canScrollChild!!.dispatchNestedFling(velocityX, velocityY, consumed)
         }

         return super.dispatchNestedFling(velocityX, velocityY, consumed)
     }
 */
    //当前ScrollView滚动的位置

    override fun fling(velocityY: Int) {
        super.fling(velocityY)
    }
    var currentScrollY: Int = 0

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        currentScrollY = t
        super.onScrollChanged(l, t, oldl, oldt)
    }
}