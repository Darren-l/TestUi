package cn.gd.snm.uitest.test02

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi


/**
 * 自定义view的Canvas总结使用。
 *
 * 画布的操作特点：
 *  1. 操作画布后，后面的绘制生效，之前的绘制不变。
 *
 *
 */
class TestCanvasView : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        initPaint()
        
        //todo 测试canvas的基本操作
//        testOperate(canvas)

        //todo 测试canvas的save保存状态的两种方式。
        testSaveStatus(canvas)

    }


    /**
     * 画布的存储方式有两种：
     *
     *  1. 存储当前的画布，存储后可以随时根据id返回，内部其实维护的是一个画布栈。
     *  2. 定义一个指定大小的画布进行离屏绘制，操作完为调用接口将离屏绘制的内容放进屏幕内。
     *
     */
    private fun testSaveStatus(canvas: Canvas?) {

        //todo 存储当前画布
//        testSave(canvas)

        //todo 定义一个指定大小的画布，必须要调用restoreToCount才能实现真正的绘制。
        testSaveLayer(canvas)
    }


    private fun testSaveLayer(canvas: Canvas?) {
        canvas!!.drawRect(200f, 200f, 700f, 700f, mPaint)

        var layerId = canvas.saveLayer(200f,200f,700f,700f,mPaint)
        mPaint.color = Color.BLUE
        var matrix = Matrix()
        matrix.setTranslate(100f,100f)
        canvas.setMatrix(matrix)

        //todo 绘制不能超过当前定义的画布大小。
        canvas.drawRect(0f,0f,800f,400f,mPaint)
        canvas.restoreToCount(layerId)

        mPaint.color = Color.BLACK
        canvas.drawRect(0f,0f,800f,400f,mPaint)
    }

    /**
     * 画布的存储状态
     *
     *  canvas.save(): 存储状态，返回当前状态码。
     *
     *  canvas.restore(): 返回上一次save的状态。若canvas.saveCount<1,调用该接口会报错。
     *
     *  canvas.restoreToCount(state)：返回指定的state画布，注意，若state前的栈画布全部会被移除。
     *
     *  canvas.saveCount: 存储的画布数。
     *
     */
    private fun testSave(canvas: Canvas?) {
        canvas!!.drawRect(0f, 0f, 700f, 700f, mPaint)
        var state = canvas.save()   //存储当前画布

        Log.e("", "onDraw: "+canvas.saveCount)

        canvas.translate(50f,50f)
        mPaint.color = Color.GRAY
        canvas.drawRect(0f,0f,500f,500f, mPaint)

        canvas.restore()    //返回上一次存储的画布
        mPaint.color = Color.BLUE
        canvas.drawRect(0f,0f,300f,300f, mPaint)


        canvas.restoreToCount(state)    //返回指定state的画布
    }

    /**
     * 画布的基本操作，平移、缩放、裁剪等。
     * 
     */
    private fun testOperate(canvas: Canvas?) {
        //todo 平移操作 -- 实际就是把画布平移，也就是变得是初始的坐标。
//        testTranslate(canvas)

        //todo 缩放画布 -- 可以带画布坐标，决定画布的初始位置。
//        testScale(canvas)

        //todo 旋转 -- 默认是以画布原点为坐标旋转，可以选择旋转点。
//        testRotate(canvas)

        //todo 倾斜 -- 正方形变菱形
//        testSkew(canvas)

        //todo 裁剪 -- 指定区域，超出区域绘制无效，区域内的可以正常显示。
//        testClipRect(canvas)

        //todo 裁剪2 -- 反向指定区域,范围内的不可以绘制。
//        testClipOutRect(canvas)

        //todo 矩阵 -- 所谓的矩阵就是把多个操作一起放到矩阵中，然后一次设置。
//        testMatrix(canvas)
    }

    private fun testMatrix(canvas: Canvas?) {
        canvas!!.drawRect(0f, 0f,700f, 700f, mPaint)
        var martix = Matrix()
        //先缩放
        martix.setScale(0.5f,0.5f)
        //然后平移
        matrix.setTranslate(300f,300f)
        canvas.setMatrix(matrix)
        mPaint.color = Color.GRAY
        canvas.drawRect(200f, 200f,700f, 700f, mPaint)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun testClipOutRect(canvas: Canvas?) {
        canvas!!.drawRect(200f, 200f,700f, 700f, mPaint)
        canvas.clipOutRect(200, 200,700, 700)  //限制绘制区域

        canvas.drawCircle(500f,500f, 100f,mPaint)
        canvas.drawCircle(100f,100f, 100f,mPaint)
        canvas!!.drawRect(300f, 300f,800f, 800f, mPaint)
    }

    private fun testClipRect(canvas: Canvas?) {
        canvas!!.drawRect(200f, 200f,700f, 700f, mPaint)
        canvas.clipRect(200, 200,700, 700)  //限制绘制区域

        //在范围内，可以正常绘制。
        canvas.drawCircle(500f,500f, 100f,mPaint)
        //不在范围内，无法绘制。
        canvas.drawCircle(100f,100f, 100f,mPaint)
        //部分在范围内，在范围内的能正常绘制。
        canvas!!.drawRect(300f, 300f,800f, 800f, mPaint)
    }

    private fun testSkew(canvas: Canvas?) {
        canvas!!.drawRect(0f,0f, 400f, 400f, mPaint)
        canvas.skew(1f,0f)  //在X方向倾斜45度,Y轴逆时针旋转45
        canvas.skew(0f, 1f) //在y方向倾斜45度， X轴顺时针旋转45
        canvas.drawRect(0f, 0f, 400f, 400f, mPaint);
    }

    private fun testRotate(canvas: Canvas?) {
        canvas!!.drawRect(200f,200f,700f,700f,mPaint)
        //
//        canvas.rotate(10f)
        canvas.rotate(10f,200f,200f)
        canvas!!.drawRect(200f,200f,700f,700f,mPaint)


        //也可以通过移动画布的方式，间接移动旋转点。
        canvas.translate(300f,300f)
        canvas!!.drawRect(0f,0f,300f,300f,mPaint)
        canvas.rotate(20f)
        canvas!!.drawRect(0f,0f,300f,300f,mPaint)
    }

    private fun testScale(canvas: Canvas?) {
        canvas!!.drawRect(0f,0f,500f,500f,mPaint)
        canvas.scale(0.5f,0.5f)

//        canvas.scale(0.5f,0.5f,200f,200f) //带移动画布初始坐标
        mPaint.color = Color.BLUE
        canvas!!.drawRect(0f,0f,500f,500f,mPaint)

    }

    private fun testTranslate(canvas: Canvas?) {
        canvas!!.drawRect(0f,0f,500f,500f,mPaint)
        canvas.translate(50f,50f)

        mPaint.color = Color.BLUE
        canvas.drawRect(0f,0f,500f,500f,mPaint)
    }

    private lateinit var mPaint:Paint
    private fun initPaint() {
        mPaint = Paint()
        mPaint.color = Color.RED
        mPaint.strokeWidth = 5f
        mPaint.style = Paint.Style.STROKE

    }

}