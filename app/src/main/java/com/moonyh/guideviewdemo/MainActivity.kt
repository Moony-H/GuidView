package com.moonyh.guideviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.constraintlayout.widget.Constraints.LayoutParams
import com.moonyh.guideview.DescriptionPosition
import com.moonyh.guideview.GuideDescriptionView
import com.moonyh.guideview.GuideLightShape
import com.moonyh.guideviewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.guideView.setLightShape(GuideLightShape.SHAPE_FIT)
        binding.guideView.setTargetViews(
            arrayOf(
                binding.test1,
                binding.test2,
                binding.test3,
                binding.test4
            )
        )


        val first = GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }
            setText("first view")
        })
            .setDescriptionPosition(DescriptionPosition.POSITION_BOTTOM)
            .setViewMargin(5.0f)
            .build()

        val second=GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }
            setText("second view")
        })
            .setDescriptionPosition(DescriptionPosition.POSITION_TOP)
            .setViewMargin(5.0f)
            .build()

        val third=GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }
            setText("third view")
        })
            .setDescriptionPosition(DescriptionPosition.POSITION_TOP)
            .setViewMargin(5.0f)
            .build()

        val descriptionViews= arrayOf(first,second,third)

        val test=GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }
            setText("third view")
        })



        binding.guideView.setDescriptionViews(descriptionViews)


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}