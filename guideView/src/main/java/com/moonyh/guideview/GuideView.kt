package com.moonyh.guideview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd

class GuideView : ConstraintLayout {


    private var targetViews: Array<View> = arrayOf()
    private var targetPadding = arrayOf<Int>()
    private var lightShape: GuideLightShape = GuideLightShape.SHAPE_FIT

    @ColorInt
    private var shadowColor = 0
    private var startWithFirstView = false

    //설명을 적는 뷰
    private var descriptionViews = arrayOf<GuideDescriptionView>()
    private var targetIndex = -1

    private val shadowView: Shadow

    private var isFirst = true

    private var onDrawReady: () -> Unit = {}

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //view group은 원래 draw를 하지 않는다. 따라서 onDraw를 override 해도 반응하지 않는다.
        //setWillNotDraw를 false로 놓으면 draw한다.

        setWillNotDraw(false)
        //하드웨어 가속
        setLayerType(LAYER_TYPE_HARDWARE, null)
        shadowView = Shadow(context, null)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GuideView)
            shadowColor = typedArray.getColor(
                R.styleable.GuideView_guideShadowColor,
                Color.parseColor("#C0000000")
            )
            val lightShape =
                GuideLightShape.values()[typedArray.getInt(R.styleable.GuideView_lightShape, 0)]
            startWithFirstView =
                typedArray.getBoolean(R.styleable.GuideView_startWithFirstView, false)
            this.lightShape = lightShape
            shadowView.setShadowColor(shadowColor)
            shadowView.setLightShape(lightShape)
            typedArray.recycle()

        }

    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (targetViews.isEmpty()) {
            //사용자가 타겟 뷰를 setTargetViews로 세팅하지 않았다면, 자식들로 기본 세팅함.
            targetViews = Array(this.childCount) {
                this.getChildAt(it)
            }
        }

        Log.e("test", "target views size: ${targetViews.size}")

        addView(shadowView)

        //사용자가 패딩을 지정하지 않았거나 적게 지정했을 때 마지막 패딩을 반복
        if (targetPadding.isEmpty()) {
            this.targetPadding = Array(targetViews.size) {
                if (this.lightShape != GuideLightShape.SHAPE_OVAL) 20 else 150
            }
        } else if (targetViews.size > targetPadding.size) {

            this.targetPadding = Array(targetViews.size) {
                if (it >= targetPadding.size) {
                    return@Array targetPadding.last()
                }
                targetPadding[it]
            }
        }


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isFirst) {
            isFirst = false
            next()
        }


    }


    private fun getBitmapFromView(view: View): Bitmap? {

        if (view.measuredWidth <= 0 || view.measuredHeight <= 0) {
            //Err
            return null
        }

        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    private fun setDescriptionPosition(targetView: View, description: GuideDescriptionView) {
        if (description.view.layoutParams == null) {
            throw IllegalStateException("description view has no layout params. please set layout params")
        }

        val desViewLp = description.view.layoutParams
        if (desViewLp.width <= 0) {
            addView(description.view)
            description.view.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    Log.e("test", "global")
                    addDescription(
                        targetView,
                        description,
                        description.view.width,
                        description.view.height
                    )
                    description.view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })
        } else {
            addDescription(targetView, description, desViewLp.width, desViewLp.height)
        }

        Log.e("test", "tag: ${description.tag}")


    }

    fun addDescription(
        targetView: View,
        description: GuideDescriptionView,
        width: Int,
        height: Int
    ) {
        when (description.tag) {
            DescriptionPosition.POSITION_TOP -> {
                //타겟이 되는 뷰의 위의 위치+ 자신의 높이 - 마진
                description.view.y =
                    (targetView.top - height).toFloat() - description.margin
                description.view.x =
                    (targetView.x + targetView.width / 2) - (width * description.anchor)
            }

            DescriptionPosition.POSITION_BOTTOM -> {
                description.view.y = (targetView.bottom).toFloat() + description.margin
                description.view.x =
                    (targetView.x + targetView.width / 2) - (width * description.anchor)
            }

            DescriptionPosition.POSITION_LEFT -> {
                description.view.x =
                    (targetView.left - width).toFloat() - description.margin
                description.view.y =
                    (targetView.y + targetView.height / 2) - (height * description.anchor)
            }

            DescriptionPosition.POSITION_RIGHT -> {
                description.view.x = (targetView.right).toFloat() + description.margin
                description.view.y =
                    (targetView.y + targetView.height / 2) - (height * description.anchor)
            }
        }
    }

    fun setTargetViews(views: Array<View>) {
        targetViews = views

    }

    fun setTargetViewsPadding(paddings: Array<Int>) {
        targetPadding = paddings
    }

    fun setDescriptionViews(descriptions: Array<GuideDescriptionView>) {
        descriptionViews = descriptions
    }


    fun next() {
        Log.e("test", "next")
        targetIndex += 1

        if (targetIndex > targetViews.size)
            return

        if (targetIndex - 1 >= 0 && descriptionViews.size > targetIndex - 1)
            removeView(descriptionViews[targetIndex - 1].view)


        //끝났으면
        if (targetIndex >= targetViews.size) {
            this.removeView(this.shadowView)
            if (targetIndex - 1 < descriptionViews.size)
                this.removeView(descriptionViews[targetIndex - 1].view)
            if (this.childCount == 0)
                ((this.parent) as ViewGroup?)?.removeView(this)
            return
        }




        if (shadowView.getLightShape() == GuideLightShape.SHAPE_FIT) {
            Log.e("test", "size fit")
            targetViews[targetIndex].apply {
                shadowView.erase(
                    (Rect(left, top, right, bottom)),
                    (getBitmapFromView(this))
                )
            }

        } else {
            Log.e("test", "else shadow erase")
            targetViews[targetIndex].apply {
                shadowView.erase(
                    Rect(left, top, right, bottom),
                    targetPadding[targetIndex]
                )
            }
        }

        if (targetIndex < descriptionViews.size) {
            Log.e("test", "set des view")
            setDescriptionPosition(targetViews[targetIndex], descriptionViews[targetIndex])
        }

    }


    fun setLightShape(shape: GuideLightShape) {
        this.lightShape = shape
        shadowView.setLightShape(shape)
    }

    fun setStartWithFirstView(isStart: Boolean) {
        this.startWithFirstView = isStart
    }

    fun setOnDrawReady(onReady: () -> Unit) {
        this.onDrawReady = onReady
    }
}

private class Shadow : View {

    private var targetPosition = Rect()
    private var shadowColor = Color.parseColor("#C0000000")
    private var targetPaddings = 20
    private var lightShape = GuideLightShape.SHAPE_RECT

    private var animator = getAnimator()

    private var prevBitmap: Bitmap? = null
    private var eraseBitmap: Bitmap? = null

    private var prevTargetPosition = Rect()

    private var onDrawReady: () -> Unit = {}

    private var prevTargetAlpha = 255
    private var nowTargetAlpha = 0

    private val erasePaint by lazy {
        Paint().apply {
            //그래픽 그리는 모드
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            color = Color.TRANSPARENT
            //투명으로 그림
            isAntiAlias = true
        }
    }

    constructor(context: Context) : this(context, null) {
        setLayerType(LAYER_TYPE_HARDWARE, null)

    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        position: Rect? = null,
        bitmap: Bitmap? = null
    ) : super(context, attrs) {
        setLayerType(LAYER_TYPE_HARDWARE, null)
        position?.let { erase(it, bitmap) }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //Log.e("test", "on draw")

        onDrawReady()
        canvas?.let {
            it.drawColor(shadowColor)
            drawShape(it)
        }
    }


    private fun drawShape(canvas: Canvas) {

        //Log.e("test", "draw shape: ${targetPositions.size}")
        when (lightShape) {
            GuideLightShape.SHAPE_FIT -> {
                this.eraseBitmap?.let { bitmap ->
                    //Log.e("test", "drawing")

                    canvas.drawBitmap(
                        bitmap,
                        targetPosition.left.toFloat(),
                        targetPosition.top.toFloat(),
                        Paint().apply { alpha = nowTargetAlpha })

                }

                this.prevBitmap?.let { bitmap ->

                    canvas.drawBitmap(
                        bitmap,
                        prevTargetPosition.left.toFloat(),
                        prevTargetPosition.top.toFloat(),
                        Paint().apply { alpha = prevTargetAlpha })
                }
            }
            GuideLightShape.SHAPE_RECT -> {
                //Log.e("test", "draw shape rect")
                canvas.drawRect(
                    targetPosition.left.toFloat() - targetPaddings,
                    targetPosition.top.toFloat() - targetPaddings,
                    targetPosition.right.toFloat() + targetPaddings,
                    targetPosition.bottom.toFloat() + targetPaddings,
                    erasePaint.apply { alpha = nowTargetAlpha }
                )
            }
            GuideLightShape.SHAPE_RECT_ROUND -> {
                //Log.e("test", "draw shape rect round")
                canvas.drawRoundRect(
                    targetPosition.left.toFloat() - targetPaddings,
                    targetPosition.top.toFloat() - targetPaddings,
                    targetPosition.right.toFloat() + targetPaddings,
                    targetPosition.bottom.toFloat() + targetPaddings,
                    30f,
                    30f,
                    erasePaint.apply { alpha = nowTargetAlpha }
                )
            }
            GuideLightShape.SHAPE_OVAL -> {
                //Log.e("test", "draw shape oval")

                canvas.drawOval(
                    targetPosition.left.toFloat() - targetPaddings,
                    targetPosition.top.toFloat() - targetPaddings,
                    targetPosition.right.toFloat() + targetPaddings,
                    targetPosition.bottom.toFloat() + targetPaddings,
                    erasePaint.apply { alpha = nowTargetAlpha }
                )
            }
        }

    }

    private fun getAnimator(): ValueAnimator = ValueAnimator.ofInt(0, 255).apply {
        duration = 500L
        addUpdateListener {
            val value = animatedValue as Int
            prevTargetAlpha = 255 - value
            nowTargetAlpha = value
            //Log.e("test","value: $value")
            invalidate()
        }
        doOnEnd {
            prevBitmap = eraseBitmap
            prevTargetPosition = targetPosition
        }
        doOnCancel {
            prevBitmap = eraseBitmap
            prevTargetPosition = targetPosition
        }
    }

    fun erase(targetPositions: Rect, paddings: Int, lightShape: GuideLightShape) {
        this.targetPosition = targetPositions
        this.lightShape = lightShape
        this.targetPaddings = paddings
    }


    fun erase(position: Rect) {
        this.targetPosition = position
        this.targetPaddings = if (lightShape == GuideLightShape.SHAPE_RECT) 20 else 90
        this.invalidate()
    }

    fun erase(position: Rect, padding: Int) {
        this.targetPaddings = padding
        this.targetPosition = position

        Log.e("test", "erase")
        this.animator.cancel()
        this.animator.start()
    }

    fun erase(position: Rect, bitmap: Bitmap?, padding: Int = 0) {
        Log.e("test", "erase: ${position.top} ${position.left}")

        this.targetPaddings = padding
        this.targetPosition = position
        this.eraseBitmap = bitmap

        this.animator.cancel()
        this.animator.start()

    }

    fun setShadowColor(@ColorInt color: Int) {
        this.shadowColor = color
    }

    fun setLightShape(lightShape: GuideLightShape) {
        this.lightShape = lightShape
    }

    fun getLightShape(): GuideLightShape = this.lightShape

    fun setOnDrawReady(onReady: () -> Unit) {
        this.onDrawReady = onReady
    }
}
