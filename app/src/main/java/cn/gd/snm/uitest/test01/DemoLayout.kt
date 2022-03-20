package cn.gd.snm.uitest.test01

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout

/**
 * 测试自定义布局。
 *
 * attrs: AttributeSet
 *
 */
class DemoLayout : RelativeLayout {

    companion object {
        val TAG: String = this::class.java.simpleName
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)


    /**
     *  该回调的MeasureSpec是父容器结合params规则计算后，得出的子容器实际可用的最大值。注意最大值并非等于该容器的实
     *  际值（如果容器是wrap，则还要计算自己子view的值），更不一定等于父容器值（即使是match_parent，还会因为父容器的
     *  padding设置而改变）。
     *
     *  如果容器是wrap那么父容器会传递这样的测量规则，由子容器自行测量得出最后的值。
     *
     *  如若当前view在xml中width和height配置的分别是match_p 和wrap_c，则mode值不一样也是因为父容器会负责传递。
     *
     *  具体原因：
     *      源码中是在setContentView中先有decor View开始启动测量，然后循环调用子控件的measure()，在measure中
     *  会调用onMeasure。
     *
     *      也就是说该回调是由父容器发起measure并传递参数后，在measure方法中调用了onMeasure走到这里的。
     *
     *      在onMeasure中，父类一般都会结合Params计算子类实际可用的最大MeasureSpec值。比如LineLayout会在自己的onMeasure
     *  方法中去计算子view的可用最大值MeasureSpec，然后调用children.measure进行传递。
     *
     *
     */
    //负责存储view的list，也可以不用存储，但是在layout时又需要重新循环遍历子view。
    var allViewList = ArrayList<List<View>>()

    //存储每一行的高度。
    var rowHeightRecord = ArrayList<Int>()

    //规避重复测量问题，只允许测量流程走一次。
    var isMeasured = false
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取到父容器size
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //获取到当前view的mode。
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        Log.d(
            TAG, "widthSize=${widthSize},heightSize=${heightSize}," +
                    "widthMode=" + "${widthMode}," + "heightMode=${heightMode}"
        )

        /**
         * 目的：测量出当前布局需要的总宽度和总高度，并调用setMeasureDimension进行确定。
         *
         *  遍历所有子控件，计算总宽高。
         *
         */
        var countHeight = 0

        //累积一行的总宽累积 -- 不停对比每一行的宽，取最大的那行
        var countRow = 0
        //同一行view存储
        var rowViewList = ArrayList<View>()
        //一行中最高的那个，决定该行的最大高度。
        var maxHeightInRow = 0
//        //宽度应该由最大的宽度的那一行决定。
        var maxWidthInRow = 0

        //todo:规避反复调用测量
        if (!isMeasured) {
            isMeasured = true

            //遍历所有子view，需要注意子view的换行。
            //TODO:我们可以通过childCount获取当前子view的数量。
            for (i in 0 until childCount) {
                var curView = getChildAt(i) //todo: 通过getChildAt循环获取子view

                //获取子控件后，要先测量子控件,否则无法获取到当前子view的宽高
                measureChild(curView, widthMeasureSpec, heightMeasureSpec)

                //todo:获取当前view的边距参数params对象
                var layoutParams = curView.layoutParams as MarginLayoutParams

                //当前子view的实际宽 = view宽 + 左边距 + 右边距
                var curViewWidth =
                    curView.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
                var curViewHeight =
                    curView.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin


                if (countRow + curViewWidth < widthSize) {
                    //Todo：不换行
                    rowViewList.add(curView)
                    //累积一行宽度
                    countRow += curViewWidth

                    //找出一行中最大的那个
                    if (maxHeightInRow < curViewHeight) {
                        maxHeightInRow = curViewHeight
                    }

                    maxHeightInRow = if (maxHeightInRow < curViewHeight) curViewHeight
                    else maxHeightInRow

                } else {
                    //todo:换行

                    //todo：注意，这里仅仅换行的第一次会走，先出来换行前的数据
                    //记录当前换行的行高，因为当前布局是自己摆放的，所以需要自行记录，用于布局阶段
                    rowHeightRecord.add(maxHeightInRow)
                    //把同一行的view存储下来，等下布局要用。
                    allViewList.add(rowViewList)

                    //总高度需要累加，由每一行最大的那个高度决定
                    countHeight += maxHeightInRow

                    //布局的最大宽度应由最宽的那一行决定
                    maxWidthInRow = if (maxWidthInRow < countRow) countRow
                    else maxWidthInRow


                    //todo:接下里处理换行后的数据更新
                    //记录当前次的宽度值
//                    countWidth = curViewWidth
                    //记录当次的高度值
                    maxHeightInRow = curViewHeight


                    //清空同一行相关数据
                    rowViewList = ArrayList<View>()
                    countRow = 0

                    //记录当前次的view
                    rowViewList.add(curView)

                    countRow += curViewWidth

                    maxWidthInRow = if (maxWidthInRow < countRow) countRow
                    else maxWidthInRow
                }

                //todo：如果是最后一行还得处理下，因为换行后非首个view，
                // 不会走上面的else部分
                if (i == childCount - 1) {
                    maxWidthInRow = if (maxWidthInRow < countRow) countRow
                    else maxWidthInRow

                    countHeight += maxHeightInRow
                    allViewList.add(rowViewList)

                    rowHeightRecord.add(countHeight)
                }
            }
        }

//        Log.d(TAG, "### width=${countWidth}, countHeight=${countHeight}")


        //todo: 兼容父控件match_patch情况,所以要判断测量模式。
        //设置控件最终的大小
        if (measureWidth == 0) {
            measureWidth = if (widthMode == MeasureSpec.EXACTLY)
                widthSize else maxWidthInRow

            measureHeight = if (heightMode == MeasureSpec.EXACTLY)
                heightSize else countHeight
        }

        //todo：最后要调用该接口，对最后的宽高进行确定 == 该接口必须要调用，
        // 否则会抛异常。
        setMeasuredDimension(measureWidth, measureHeight)
    }

    var measureWidth = 0
    var measureHeight = 0


    /**
     * 该回调的作用是，具体的去执行view的摆放，参数为父容器的摆放坐标。
     *
     * 目标：
     *  最后调用view.layout对view进行布局。
     *
     * 摆放的规则：
     *  1.布局方式为传入具体的左上角和右下角的坐标给到layout()方法。
     *  2. 如果涉及到子view的换行，需要在测量的时候记录换行的高度，因为getChildAt方法获取到的子
     *  view仅仅只能拿到view相关信息。
     *
     *
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, l, t, r, b)
        Log.d(TAG, "onlayout: left=${left}, top=${top}, right=${right}, bottom=${bottom}")

        //目的是确定每个子view的上左下右的具体值，然后调用layout接口进行布局。
        var curTop = 0
        var curLeft = 0
        var curBottom = 0
        var curRight = 0

        var countLeftOnRow = 0
        var countTop = 0

//        //TODO：最后要调用布局。
//        view.layout(curLeft, curTop, curRight, curBottom)

        if (allViewList.size == 0) {
            return
        }

        //遍历每一行
        for (index in allViewList.indices) {
            for (view in allViewList[index]) {
                var params = view.layoutParams as MarginLayoutParams

                //当前view的左上右下。
                curLeft = params.leftMargin + countLeftOnRow
                curTop = params.topMargin + countTop

                curRight = curLeft + view.measuredWidth

                //TODO：注意，这里不需要加bottom的边距，记住layout()
                // 接口要的就是当前 view实际的左上和右下的坐标。
                curBottom = view.measuredHeight + curTop

                //累积左边距 左边view的宽度+左边view的左边距 + 左边view的右边距
                countLeftOnRow = curLeft + view.measuredWidth

                //TODO：最后要调用布局。
                view.layout(curLeft, curTop, curRight, curBottom)
            }

            //一行结束后，需要累积高度
            countTop += rowHeightRecord[index]
            //需要清空左边距累积
            countLeftOnRow = 0
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}