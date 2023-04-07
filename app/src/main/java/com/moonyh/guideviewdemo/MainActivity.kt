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
                binding.test4,
                binding.icon
            )
        )

        binding.guideView.setOnClickListener {
            binding.guideView.next()

        }
        val first=GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }
            setText("구석에 있는 이미지 가능합니다.")
            setTextSize(5)

        })
            .setDescriptionPosition(DescriptionPosition.POSITION_TOP)
            .setViewMargin(50.0f)
            .setViewAnchor(0.1f)
            .build()

        val second = GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }

            setText("텍스트를 하이라이트 할 수 있습니다.")
            setTextSize(5)

        })
            .setDescriptionPosition(DescriptionPosition.POSITION_BOTTOM)
            .setViewMargin(50.0f)
            .setViewAnchor(0.5f)
            .build()



        val third=GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }
            setText("anchor, margin을 통해\n위치를 조절할 수 있습니다.")

            setTextSize(5)

        })
            .setDescriptionPosition(DescriptionPosition.POSITION_TOP)
            .setViewMargin(5.0f)
            .setViewAnchor(1.0f)
            .build()

        val forth=GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }
            setText("top,bottom,left,right로 위치를 설정할 수 있습니다.")
            setTextSize(5)
        })
            .setDescriptionPosition(DescriptionPosition.POSITION_BOTTOM)
            .setViewMargin(5.0f)
            .build()

        val fifth=GuideDescriptionView.Builder(BubbleDescriptionView(baseContext).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnNextClicked { binding.guideView.next() }
            setText("복잡한 이미지도 가능합니다.")
            setTextSize(5)
        })
            .setDescriptionPosition(DescriptionPosition.POSITION_RIGHT)
            .setViewMargin(5.0f)
            .build()



        val descriptionViews= arrayOf(first,second,third,forth,fifth)

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