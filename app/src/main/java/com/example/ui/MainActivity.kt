package com.example.ui

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var barHeight : Int = 0
    /**
     * Activity 管理界面的生命周期  接收事件（touch事件  触摸事件）
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //获取屏幕的尺寸
        val display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        Log.v("swl","屏幕尺寸 width:${display.widthPixels}, height : ${display.heightPixels}")

        //获取内容绘制区域的尺寸
        val drawRect = Rect()
        //通过获取window上的content容器 ->容器的rect
        window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).getDrawingRect(drawRect)

        //顶部高度
        val barHeight = display.heightPixels - drawRect.height()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    //触摸事件回调
    /**
     * 事件类型  MotionEvent类来管理
     * ACTION_DOWN  按下
     * ACTION_UP    离开屏幕
     * ACTION_MOVE   移动
     * ACTION_CANCEL  被其他应用打断
     *
     * activity   dispatchTouchEvent
     * phoneWindows   superDispatchTouchEvent
     * decorView  superDispatchTouchEvent
     * ViewGroup   dispatchTouchEvent
     *        查找可以接收事件的targets
     *       dispatchTransformedTouchEvent 遍历给一个target
     *       child. dispatchTouchEvent(event)  将事件分发给这个target
     *       onTouchEvent(event)
     *       如果子空间需要接收触摸事件 就必须实现onTouchEvent
     *
     *       当返回值为true时，表示这个事件已经被消费了，就不会继续传递
     *       当返回值为false时，表示这个事件没有被消费，就继续传递
     */


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
               // Log.v("swl", "手指按下 x: ${event.x},y:${event.y}")
               // changeColor(event)
                changeColor2(event)
            }
            MotionEvent.ACTION_MOVE -> {
             //   Log.v("swl","手指滑动")
               // changeColor(event)
                changeColor2(event)
            }
            MotionEvent.ACTION_UP ->{
              //  Log.v("swl","手指离开屏幕")
            }else -> Log.v("swl","被其他应用打断了")
        }
        return true
    }

    //计算顶部的高度
    //将触摸点的y坐标减去顶部高度 ->获得相对于内容绘制区域的坐标
   private fun changeColor2(event: MotionEvent){
      //获取视图的rect
       val viewRect = Rect(mView.left,mView.top,mView.right,mView.bottom)

        //将触摸点的y坐标 - 顶部高度 = 相对于内容绘制区域的坐标
        viewRect.contains(event.x.toInt(),(event.y-barHeight).toInt()).also {
            if(it){
                mView.background= getDrawable(R.color.colorPrimary)
            }else{
                mView.background = getDrawable(R.color.colorAccent)
            }
        }
   }

    private fun changeColor(event: MotionEvent){
        //获取触摸点的x和y坐标
        //event.x  表示触摸点和屏幕的左边间距
        //event.y  表示触摸点和屏幕的右边间距

        /**
         * Rect(l,t,r,b)
         * l:x,
         * t:y,
         * r:l + width ,
         * b: t + height
         * 触摸点 Point(x,y)
         * mView.x  相对于父容器的左边间距
         * mView.y  相对于父容器的顶部间距   没有bar和状态栏的高度
         *
         */
        val rect:Rect  = Rect()
        //获取这个控件在屏幕上的rect  相对于屏幕来说的尺寸
        mView.requestRectangleOnScreen(rect)
        rect.right = rect.left + mView.width
        rect.bottom = rect.top + mView.height
        Log.v("swl","手指按下  x: ${rect.left}  y:${rect.top} right:${rect.right} bottom: ${rect.bottom} ")
        //判断某个rect是否包含某个点
        rect.contains(event.x.toInt(),event.y.toInt()).also {
            if(it){
                mView.background= getDrawable(R.color.colorPrimary)
            }else{
                mView.background = getDrawable(R.color.colorAccent)
            }
        }

        Log.v("swl","视图坐标 x: ${rect.left},y:${rect.top}")
    }
    //当触摸事件开始的时候  优先回调这个方法
    override fun onUserInteraction() {
        super.onUserInteraction()
     //   Log.v("swl","触摸事件即将开启")
    }



    fun test(){
        /**
        android:alpha="0.5"   透明度  1 ->不透明   0->全透明
        android:background="@android:color/holo_purple"  背景（图片资源  固定的图片  xml配置文件）
        android:clickable="true"  设置这个控件是否可以点击  （和用户交互）
        android:focusable="true"  设置是否可以获取焦点
        android:tag="1"  标签，字符串，1.记录数据  2.通过标签获取控件

        android:onClick="changeBg"  给控件添加点击事件  不建议使用（不稳定）
         */
        //当有id时可以直接通过id访问控件  ->kotlin才行
        //mView

        //当id不确定时  可以通过tag访问控件
        /**
        val mView =   container.findViewWithTag<View>("1")
        mView.alpha=1f
        mView.background = getDrawable(R.color.colorAccent)
         */

        //2.通过实现对应的接口 来实现监听事件,回调给这个类的对象本身（自己监听事件）
        //  mView.setOnClickListener(this)

        //3.直接声明一个类   实现对应的接口和方法  回调给另外一个对象（别人监听）
        //  mView.setOnClickListener(MyListener())

        //4.匿名内部类
        /**
        mView.setOnClickListener(object :View.OnClickListener{
        override fun onClick(v: View?) {
        v?.background= getDrawable(R.color.colorPrimary)
        }
        })
         */

        //5.如果实现的接口只有一个 可以使用lambda表达式
        /**
        mView.setOnClickListener({v:View? ->
        v?.background= getDrawable(R.color.colorPrimary)
        })
         */
        //6.如果这个方法的最后一个参数是lambda表达式，那么这个表达式可以放在括号外边
        /**
        mView.setOnClickListener{v:View? ->
        v?.background= getDrawable(R.color.colorPrimary)
        }
         */

        /**
        //创建对象
        val myView = MyView()
        //接收回调
        myView.callBack = {
        Log.v("swl","主页接收到回调的数据了：$it")
        }

        //7.如果这个方法只有一个参数，那么这个参数可以省略
        mView.setOnClickListener{
        //  it?.background= getDrawable(R.color.colorPrimary)
        myView.performClick()
        }
         */

        /**
        inner class MyListener:View.OnClickListener {
        override fun onClick(v: View?) {
        v?.background= getDrawable(R.color.colorPrimary)
        }
        }
         */
        /**
        override fun onClick(v: View?) {
        v?.background= getDrawable(R.color.colorPrimaryDark)
        }
         */

        //通过onClick设置点击事件
        /**
        fun changeBg(view: View) {
        view.background=getDrawable(R.color.colorPrimary)
        }
         */
    }
}
