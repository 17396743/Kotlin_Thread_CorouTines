package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val TAG: String? = ".MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(TAG, "主线程id：${mainLooper.thread.id}")
        //runBlocking启动的协程任务会阻断当前线程，
        // 直到该协程执行结束。
        // 当协程执行结束之后，
        // 页面才会被显示出来。
        RunBlocking()
        //launch不会阻断主线程。
        Launch()
        //suspend函数会将整个协程挂起，
        // 而不仅仅是这个suspend函数，
        // 也就是说一个协程中有多个挂起函数时，
        // 它们是顺序执行的。
        SusPendData()
        //async跟launch的用法基本一样，
        // 区别在于：async的返回值是Deferred，
        // 将最后一个封装成了该对象。
        // async可以支持并发 ，
        // 此时一般都跟await一起使用
        AsyncData()
        Log.e(TAG, "协程执行结束")
    }

    private fun AsyncData() {
        GlobalScope.launch {
            val result1 = GlobalScope.async {
                getResult1()
            }
            val result2 = GlobalScope.async {
                getResult2()
            }
            val result = result1.await() + result2.await()
            Log.e(TAG, "result = $result")
        }
    }

    private suspend fun getResult1(): Int {
        delay(3000)
        return 1
    }

    private suspend fun getResult2(): Int {
        delay(4000)
        return 2
    }


    private fun SusPendData() {
        GlobalScope.launch {
            val token = getToken()
            val userInfo = getUserInfo(token)
            setUserInfo(userInfo)
        }
        repeat(8) {
            Log.e(TAG, "主线程执行$it")
        }

    }

    private fun setUserInfo(userInfo: String) {
        Log.e(TAG, userInfo)
    }

    private suspend fun getToken(): String {
        delay(2000)
        return "token"
    }

    private suspend fun getUserInfo(token: String): String {
        delay(2000)
        return "$token - userInfo"
    }

    private fun Launch() {
        Log.e(TAG, "主线程id：${mainLooper.thread.id}")
        val job = GlobalScope.launch {
            delay(6000)
            Log.e(TAG, "协程执行结束 -- 线程id：${Thread.currentThread().id}")
        }
        Log.e(TAG, "主线程执行结束")
    }

    private fun RunBlocking() = runBlocking {
        //运行次数为8次
        repeat(8) {
            Log.e(TAG, "协程执行$it 线程id：${Thread.currentThread().id}")
            //延时1000毫秒
            delay(1000)
        }
    }

    //相关资料：https://www.jianshu.com/p/6e6835573a9c


}