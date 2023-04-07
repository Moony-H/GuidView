package com.moonyh.guideviewdemo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.moonyh.guideviewdemo.databinding.SourceCustomBubbleDescriptionBinding
import kotlin.math.roundToInt

class BubbleDescriptionView:CardView {

    private val binding:SourceCustomBubbleDescriptionBinding

    private var onPrevClicked={_: View-> }
    private var onNextClicked={_: View-> }


    constructor(context:Context):this(context,null)
    constructor(context: Context,attrs:AttributeSet?):super(context, attrs){
        binding=SourceCustomBubbleDescriptionBinding.inflate(LayoutInflater.from(context),this)

        this.setCardBackgroundColor(ContextCompat.getColor(context,R.color.white))
        this.radius=convertDpToPx(15).toFloat()

        binding.nextButton.setOnClickListener(onNextClicked)
        //binding.prevButton.setOnClickListener(onPrevClicked)
    }


    fun setOnNextClicked(onClicked: (View) -> Unit){
        this.onNextClicked=onClicked
        binding.nextButton.setOnClickListener(onNextClicked)

    }

//    fun setOnPrevClicked(onClicked:(View)->Unit){
//        this.onPrevClicked=onClicked
//        binding.prevButton.setOnClickListener(onPrevClicked)
//
//    }

    fun setText(text:String){
        binding.text.text = text
    }

    fun setTextSize(sizeDp:Int){
        binding.text.textSize=convertDpToPx(sizeDp).toFloat()
    }


    private fun convertDpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).roundToInt()
    }
}