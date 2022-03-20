package cn.gd.snm.uitest.test02

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * DarrenAdd：测试自定义view中的图层混合模式 -- 离屏绘制。
 *
 *  所谓的混合模式，就是两个图层叠加在一起时，取交集还是并集。
 *
 */
class TestLayerView: View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        test(canvas)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    /**
     *
     * DarrenAdd：混合模式 -- 选择两个重叠图形到底要取哪部分作为最后的保留在canvas的结果。
     *
     *  PorterDuff.Mode.DST_IN：设置混合模式后，会“干扰”后一个图形的绘制。canvas图形取交集，
     *  而paint属性则是选择已经绘制的那个。（也就是会干扰后一个图形颜色设置的不生效）
     *
     */
    private fun test(canvas: Canvas?) {
        //初始化画笔
        var mPaint = Paint()
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.FILL_AND_STROKE

        //禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setBackgroundColor(Color.GRAY)

        /**
         * 离屏绘制原因：当前已经存在的绘制会影响到离屏绘制的结果。
         *
         * 离屏绘制步骤：
         *  1. 获取图层id -》绘制第一个图层 -》 选择混合模式 -》
         *
         *  绘制第二个图层 -》清除混合模式 -》存储此次图层到canvas
         *
         */

        //开始离屏绘制 -- todo: 目的是摒弃画布上已经存在的元素影响，只考虑设置离屏后的操作。
        var layerId = canvas!!.saveLayer(0f,0f, width.toFloat(),
            height.toFloat(),
            mPaint, Canvas.ALL_SAVE_FLAG)
        //开始绘制第一个图 -- 绘制蓝色的矩形。
        canvas!!.drawBitmap(createRectBitmap(measuredWidth, measuredHeight)!!,
            0f, 0f, mPaint)
        //todo: 设置混合模式，这里设置的是取交集.
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        //todo:开始绘制第二个图 -- 绘制黄色的圆形，但接受了混合模式，所以并非绘制，
        // 而是直接取了和上一次绘制的交集部分。
        canvas.drawBitmap(createCircleBitmap(measuredWidth, measuredHeight)!!,
            0f, 0f, mPaint)
        //清除混合模式
        mPaint.xfermode = null

        //todo:将此次离屏绘制的结果给到canvas
        canvas.restoreToCount(layerId)
    }


    //绘制矩形demo
    private fun createRectBitmap(width: Int, height: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val dstPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dstPaint.color = -0x995501
        canvas.drawRect(Rect(width / 20, height / 3, 2 * width / 3, 19 * height / 20), dstPaint)
        return bitmap
    }

    //绘制圆形demo
    private fun createCircleBitmap(width: Int, height: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val scrPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        scrPaint.color = -0x33bc
        canvas.drawCircle(
            (width * 2 / 3).toFloat(),
            (height / 3).toFloat(),
            (height / 4).toFloat(),
            scrPaint
        )
        return bitmap
    }
}