package cn.gd.snm.uitest.test02

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import cn.gd.snm.uitest.R

/**
 * DarrenAdd：paint画笔的常用接口。
 *
 * 1. canvas，paint常用接口的使用。
 * 2. 梯度渲染使用。
 *
 */
class TestPaintview : View {

    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mPaint: Paint? = null

    /**
     * paint画笔的常用接口。
     *
     */
    private fun initPaint() {
        mPaint = Paint()
        // 设置颜色
        mPaint!!.color = Color.RED
        // 设置 Paint对象颜色,范围为0~255
        mPaint!!.setARGB(255, 255, 255, 0)
        // 设置alpha不透明度,范围为0~255
//        mPaint!!.alpha = 200
        // 抗锯齿
        mPaint!!.isAntiAlias = true
        //描边效果
        mPaint!!.style = Paint.Style.FILL
        //描边宽度
//        mPaint!!.strokeWidth = 4f
        //圆角效果
//        mPaint!!.strokeCap = Paint.Cap.ROUND
        //拐角风格
//        mPaint!!.strokeJoin = Paint.Join.MITER
        //设置环形渲染器
//        mPaint!!.shader = SweepGradient(200, 200, Color.BLUE, Color.RED)
        //设置图层混合模式
//        mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)
        //设置颜色过滤器
//        mPaint!!.colorFilter = LightingColorFilter(0x00ffff, 0x000000)
        //设置双线性过滤
//        mPaint!!.isFilterBitmap = true
        //设置画笔遮罩滤镜 ,传入度数和样式
//        mPaint!!.maskFilter = BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL)
        // 设置文本缩放倍数
//        mPaint!!.textScaleX = 2f
        // 设置字体大小
//        mPaint!!.textSize = 38f
        //对其方式
//        mPaint!!.textAlign = Paint.Align.LEFT
        // 设置下划线
//        mPaint!!.isUnderlineText = true
//        val str = "Test测试"
//        val rect = Rect()
        //测量文本大小，将文本大小信息存放在rect中
//        mPaint!!.getTextBounds(str, 0, str.length, rect)
        //获取文本的宽
//        mPaint!!.measureText(str)
        //获取字体度量对象
//        mPaint!!.fontMetrics
    }

    /**
     * paint画笔：决定图的填充元素，如填充颜色，填充方式（描边，填充），抗锯齿等。还可以通过shader的方式加载图片。
     *
     * canvas画布：决定图的形状。
     *
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //todo：普通绘制
        //绘制一个矩形
//        drawRect(canvas)
        //绘制一个圆形
//        drawCircle(canvas)
        //其他绘制。
//        drawOther(canvas)

        //todo: 梯度渲染
        //线性梯度渲染 -- 特点直线梯度 -- 支持多种颜色，
        // 指定过度坐标渲染。
        drawShaderLinearGradient(canvas)
        //环形梯度渲染 -- 特点从外到内梯度
//        drawShaderRadialGradient(canvas)
        //扫描梯度渲染 -- 特点按指针旋转梯度
//        drawSweepGradient(canvas)
        //位图渲染 -- 特点可加载图片
//        drawBitmapShader(canvas)
        //组合位图渲染 -- 在普通位图上加上一些颜色的“滤镜”
        draBitmapShader2(canvas)
    }

    /**
     * 组合渲染，
     * ComposeShader(@NonNull Shader shaderA, @NonNull Shader shaderB, Xfermode mode)
     * ComposeShader(@NonNull Shader shaderA, @NonNull Shader shaderB, PorterDuff.Mode mode)
     * shaderA,shaderB:要混合的两种shader
     * Xfermode mode： 组合两种shader颜色的模式
     * PorterDuff.Mode mode: 组合两种shader颜色的模式
     */
    private fun draBitmapShader2(canvas: Canvas?) {
        var paint = Paint()
        var mBitmap = BitmapFactory.decodeResource(resources, R.drawable.mm)

        val bitmapShader = BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        val linearGradient = LinearGradient(
            0f,
            0f,
            1000f,
            1600f,
            intArrayOf(Color.RED, Color.GREEN, Color.BLUE),
            null,
            Shader.TileMode.CLAMP
        )
        var mShader = ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY)
        paint.shader = mShader
        canvas!!.drawCircle(250f, 250f, 250f, paint)
    }

    /**
    * 位图渲染，BitmapShader(@NonNull Bitmap bitmap, @NonNull TileMode tileX, @NonNull TileMode tileY)
    * Bitmap:构造shader使用的bitmap
    * tileX：X轴方向的TileMode
    * tileY:Y轴方向的TileMode
        REPEAT, 绘制区域超过渲染区域的部分，重复排版
        CLAMP， 绘制区域超过渲染区域的部分，会以最后一个像素拉伸排版
        MIRROR, 绘制区域超过渲染区域的部分，镜像翻转排版
    */
    private fun drawBitmapShader(canvas: Canvas?) {
        var paint = Paint()
        var mBitmap = BitmapFactory.decodeResource(resources, R.drawable.mm)
        var mShader = BitmapShader(mBitmap, Shader.TileMode.REPEAT,
            Shader.TileMode.MIRROR)
        paint.shader = mShader
//        canvas!!.drawRect(0f, 0f, 800f, 800f, paint)
        canvas!!.drawCircle(250f, 250f, 250f, paint)
    }

    /**
    * 扫描渲染，SweepGradient(float cx, float cy, @ColorInt int color0,int color1)
    * cx,cy 渐变中心坐标
    * color0,color1：渐变开始结束颜色
    * colors，positions：类似LinearGradient,用于多颜色渐变,positions为null时，根据颜色线性渐变
    */
    private fun drawSweepGradient(canvas: Canvas?) {
        var paint = Paint()
        var mShader = SweepGradient(250f, 250f, Color.RED, Color.GREEN)
        paint.shader = mShader
        canvas!!.drawCircle(250f, 250f, 250f, paint)
    }

    /**
     * 环形渲染，RadialGradient(float centerX, float centerY, float radius, @ColorInt int colors[], @Nullable float stops[], TileMode tileMode)
     * centerX ,centerY：shader的中心坐标，开始渐变的坐标
     * radius:渐变的半径
     * centerColor,edgeColor:中心点渐变颜色，边界的渐变颜色
     * colors:渐变颜色数组
     * stoops:渐变位置数组，类似扫描渐变的positions数组，取值[0,1],中心点为0，半径到达位置为1.0f
     * tileMode:shader未覆盖以外的填充模式。
     */
    private fun drawShaderRadialGradient(canvas: Canvas?) {

        var paint = Paint()

        var mShader = RadialGradient(
            250f,
            250f,
            250f,
            intArrayOf(Color.GREEN, Color.YELLOW, Color.RED),
            null,
            Shader.TileMode.CLAMP
        )
        paint.shader = mShader
        canvas!!.drawCircle(250f, 250f, 250f, paint)
    }

    /**
     * 1.线性渲染,LinearGradient(float x0, float y0, float x1, float y1,
     * @NonNull @ColorInt int colors[], @Nullable float positions[], @NonNull TileMode tile)
     *
     * (x0,y0)：渐变起始点坐标
     * (x1,y1):渐变结束点坐标
     * color0:渐变开始点颜色,16进制的颜色表示，必须要带有透明度
     * color1:渐变结束颜色
     * colors:渐变数组
     * positions:位置数组，position的取值范围[0,1],作用是指定某个位置的颜色值，如果传null，渐变就线性变化。
     * tile:用于指定控件区域大于指定的渐变区域时，空白区域的颜色填充方法
     */
    private fun drawShaderLinearGradient(canvas: Canvas?) {
        var sharder = LinearGradient(0f,0f,250f,250f,
            Color.RED,Color.BLUE,Shader.TileMode.CLAMP)
        var paint = Paint()

        paint.shader = sharder
        canvas!!.drawRect(Rect(0,0,500,500),paint)

        //demo2
        var sharder2 = LinearGradient(
            0f,
            0f,
            500f,
            500f,
            intArrayOf(Color.RED, Color.BLUE, Color.GREEN),
            floatArrayOf(0f, 0.7f, 1f),
            Shader.TileMode.REPEAT
        )
        paint.shader = sharder2
//        canvas.drawCircle(250, 250, 250, mPaint);
//        canvas.drawCircle(250, 250, 250, mPaint);
        canvas.drawRect(0f, 0f, 1000f, 1000f, paint)
    }

    private fun drawOther(canvas: Canvas?) {
        var paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE

        paint.textSize = 100f
        //画一条之直线
        canvas!!.drawLine(100f,100f,200f,200f,paint)

        //画一个点，这个点的坐标是文字的左下角开始。
        canvas.drawText("测试1",500f,500f,paint)

    }

    private fun drawCircle(canvas: Canvas?) {
        var paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE

        canvas!!.drawCircle(500f,500f,200f,paint)

        canvas.drawPoint(500f,500f,paint)

        canvas.drawCircle(250f,500f,200f,paint)

    }

    private fun drawRect(canvas: Canvas?) {
        var paint = Paint()
        paint.color = Color.RED
        //设置填充方式，默认为fill
        paint.style = Paint.Style.STROKE
        //如果是描边，则可以设置描边宽度
        paint.strokeWidth = 50f

        var rect = Rect(0, 0, 200, 200)
        canvas!!.drawRect(rect, paint)
    }


}