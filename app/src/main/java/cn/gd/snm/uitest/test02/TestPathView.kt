package cn.gd.snm.uitest.test02

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Path及贝塞尔曲线
 *
 */
class TestPathView : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
        initPath()

        //todo path画直线
//        testLineByPath(canvas)

        //todo path画圆、圆弧、椭圆等。
//        testPath(canvas)

        //todo 二阶贝塞尔曲线
//        secOrderBezier(canvas)
        
        //todo 三阶贝塞尔曲线
        thirOrderBezier(canvas)


    }

    private fun testPath(canvas: Canvas?) {
        var path = Path()
        //圆弧
//        path.addArc(200f, 200f, 400f, 400f,
//            -225f, 225f)

        //绘制矩形 -- CW为顺时针绘制
//        path.addRect(500f, 500f, 900f, 900f,
//            Path.Direction.CW)

        //绘制圆
//        path.addCircle(700f,700f, 200f, Path.Direction.CW)

        //绘制椭圆 -- CCW为逆时针绘制
//        path.addOval(0f,0f,500f,300f,
//            Path.Direction.CCW)

        //XXto的接口为追加图形
        //todo false会自动连接上一个图形的末端点，true为move不连接
//        path.arcTo(400f, 200f, 600f, 400f,
//            -180f, 225f, false)

        //普通矩形为直接，以下类为添加一个圆角矩形
//        var rectf =  RectF(200f, 800f, 700f, 1200f)
//        path.addRoundRect(rectf, 20f, 20f, Path.Direction.CCW)

        canvas!!.drawPath(path,mPaint)
    }

    private fun thirOrderBezier(canvas: Canvas?) {
        var path = Path()
        path.moveTo(300f, 500f)
        path.rCubicTo(200f, -400f, 300f, 700f, 500f, 0f)
        canvas!!.drawPath(path,mPaint)
    }

    private fun secOrderBezier(canvas: Canvas?) {
        var path = Path()
        path.moveTo(300f, 500f)
        path.quadTo(500f, 100f, 800f, 500f)
        //todo 所有带r开头的接口，都是基于上次move的相对坐标
//        path.rQuadTo(200f, -400f, 500f, 0f)
        canvas!!.drawPath(path,mPaint)
    }

    private fun testLineByPath(canvas: Canvas?) {
        //todo path相当于路径
        var path = Path()
        path.moveTo(100f, 100f)

        //todo 此时可以选择相对坐标或者绝对坐标
        //原点为坐标
        path.lineTo(100f,500f)
        //相对move后的坐标
//        path.rLineTo(0f,400f)

        path.lineTo(700f,500f)
        //todo 调用close后会自动连接收尾，闭合。
        path.close()
        canvas!!.drawPath(path, mPaint)
    }

    private lateinit var mPaint:Paint;
    private fun initPath() {
        mPaint = Paint()
        mPaint.color = Color.RED
        mPaint.strokeWidth = 5f
        mPaint.style = Paint.Style.STROKE
    }
}