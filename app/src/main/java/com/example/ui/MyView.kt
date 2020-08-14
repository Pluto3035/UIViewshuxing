package com.example.ui

/**
 * @Description
 * 代码高手
 */
class MyView {
    //1.定义接收回调的函数/方法
     var callBack:((String)->Unit)?= null

    //2.事件触发
    fun performClick(){
        callBack?.let {
            it("user:swl")
        }
    }
}