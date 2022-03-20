package cn.gd.snm.uitest.test02

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import cn.gd.snm.uitest.R
import cn.gd.snm.uitest.test02.tools.ColorFilter


/**
 * DarrenAdd: 自定义view的颜色过滤器，通常用于我们对照片的一些滤镜编辑，如复古主题等。
 *
 */
class TestColorFilter : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        initPaint()

        //todo: 删除过滤指定颜色
        deleteSpeColor(canvas)

        //todo：指定颜色更亮
//        lightSpeColor(canvas)

        //todo: 混合多个颜色
//        mixSpeColor(canvas)

        //todo：矩阵颜色，可同时设置RGBA程度
        // 通常用于滤镜的特效
//        matrixColor(canvas)
    }

    //todo: 默认的原图矩阵
    var colorMatrix = floatArrayOf(
        1f, 0f, 0f, 0f, 0f,     //红色比重 -- 若改2f，则图片显示更绿
        0f, 1f, 0f, 0f, 0f,     //绿色比重
        0f, 0f, 1f, 0f, 0f,     //蓝色比重
        0f, 0f, 0f, 1f, 0f)     //透明度比重

    /**
     * 自定义颜色矩阵，可以更加细微的调整图片的色调。
     *
     */
    private fun matrixColor(canvas: Canvas?) {
        var mColorMatrixColorFilter = ColorMatrixColorFilter(ColorFilter
            .colormatrix_heibai)

        paint.colorFilter = mColorMatrixColorFilter
        canvas!!.drawBitmap(bitmap, 100f, 0f, paint)

        //修改矩阵的方式除了初始化矩阵并设置，还能使用api。
        val cm = ColorMatrix()
        cm.setScale(2f,1f,1f,1f)

        //设置色调
        cm.setSaturation(2f)

        //设置色调 -- 将rgb看做三系坐标，进行旋转度数的转化，这里相当于转了50度角
        cm.setRotate(0,60f)


    }

    private fun mixSpeColor(canvas: Canvas?) {
        val porterDuffColorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN)
        paint.colorFilter = porterDuffColorFilter
        canvas!!.drawBitmap(bitmap, 100f, 0f, paint)
    }


    private fun lightSpeColor(canvas: Canvas?) {
        //绿色更亮
        val lighting = LightingColorFilter(0xffffff, 0x003000)
        paint.colorFilter = lighting
        canvas!!.drawBitmap(bitmap, 0f, 0f, paint)
    }

    private fun deleteSpeColor(canvas: Canvas?) {

        //原图 -- 参数为0xffffff
//        val lighting = LightingColorFilter(0xffffff, 0x000000)
//        paint.colorFilter = lighting
//        canvas!!.drawBitmap(bitmap, 0f, 0f, paint)

        //红色去除掉
        val lighting = LightingColorFilter(0x00ffff, 0x000000)
        paint.colorFilter = lighting
        canvas!!.drawBitmap(bitmap, 0f, 0f, paint)
    }

    private lateinit var paint:Paint
    private lateinit var bitmap: Bitmap

    @SuppressLint("ResourceType")
    private fun initPaint() {
        paint = Paint()

        //todo: 解决图片显示不全问题
        var typedValue = TypedValue()
        resources.openRawResource(R.drawable.ic_test, typedValue)
        var option = BitmapFactory.Options()
        option.inTargetDensity = typedValue.density
        option.inScaled = false

        bitmap = BitmapFactory.decodeResource(resources,
            R.drawable.ic_test,option)
    }

}