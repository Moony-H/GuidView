package com.moonyh.guideview

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams


//설명이 들어갈 뷰의 빌더
class GuideDescriptionView private constructor(
    val tag:DescriptionPosition,
    val anchor:Float,
    val margin:Float,
    val view: View
) {


    class Builder {
        private val view:View

        constructor(view: View){
            this.view=view
        }

        constructor(text:String,context:Context){
            this.view=AppCompatTextView(context).apply { setText(text) }
            this.view.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            this.view.layoutParams=LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        }

        private var tag = DescriptionPosition.POSITION_BOTTOM
        private var anchor = 0.5f
        private var margin = 0f

        //오른쪽 왼쪽 위 아래 선택하여 tag 주기
        fun setDescriptionPosition(tag: DescriptionPosition): Builder {
            this.tag = tag
            return this
        }

        //앵커를 주어 얼만큼 기울어져 있는 상태로 줄지 선택
        fun setViewAnchor(anchor: Float): Builder {
            this.anchor = anchor
            return this
        }

        //기준되는 곳과 얼마나 떨어져 있을 지
        fun setViewMargin(margin: Float): Builder {
            this.margin = margin
            return this
        }

        //빌드
        fun build() = GuideDescriptionView(tag, anchor, margin,view)
    }
}