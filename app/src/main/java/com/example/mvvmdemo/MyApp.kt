package com.example.mvvmdemo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.sigmob.windad.OnInitializationListener
import com.sigmob.windad.OnStartListener
import com.sigmob.windad.WindAdOptions
import com.sigmob.windad.WindAds
import com.sigmob.windad.WindCustomController
import com.tencent.mmkv.MMKV
import xcrash.XCrash

class MyApp : Application() {

    companion object {
        //static 代码段可以防止内存泄露
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//                layout.setPrimaryColorsId(R.color.purple_500, android.R.color.white) //全局设置主题颜色
                ClassicsHeader(context) //指定为经典Header，默认是 贝塞尔雷达Header
            }

            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                ClassicsFooter(context).setDrawableSize(20f) //指定为经典Footer，默认是 BallPulseFooter
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks()
        // 初始化广告sdk
        if (hasAgreedPrivacy()) {
            initSigMobSdk()
        }
        XCrash.init(this)
//        MMKV
        MMKV.initialize(this)
    }


    fun initSigMobSdk() {
        // 替换为您的实际AppID和AppKey
        /**
         *  Sigmob Public Key 95c505808825306bba57271b66f81633
         *  Sigmob Secret Key 79229e7cf20e4f68ed45a6a9fda27f95
         *  ToBid Public Key 7bab3ee4a51b63a59e95fa8f0bd37224
         *  ToBid Secret Key 4d577b396fa3fd1621e21ad622ea052b
         */
        val appId = "6878"
        val appKey = "8ebc1fd1c27e650c"

        val ads = WindAds.sharedAds()
        val options = WindAdOptions(appId, appKey)
        options.setCustomController(object : WindCustomController() {
            /**
             * 是否允许 SDK 主动获取 IMEI
             *
             * @return true 可以使用，false 禁止使用。默认为 true
             */
            override fun isCanUsePhoneState(): Boolean {
                return false
            }

            override fun getDevImei(): String {
                return "867400022047199"
            }

            override fun isCanUseSimOperator(): Boolean {
                return false
            }

            override fun isCanUseOaid(): Boolean {
                return true
            }

            override fun isCanUseAndroidId(): Boolean {
                return true
            }


        })
        ads.init(this, options, object : OnInitializationListener {
            override fun onInitializationSuccess() {
                LogUtils.d("初始化成功")
                ads.start(object : OnStartListener {
                    override fun onStartSuccess() {
                        LogUtils.d("启动成功")
                    }

                    override fun onStartFail(error: String?) {
                        LogUtils.d("启动失败")
                    }

                })
            }

            override fun onInitializationFail(error: String?) {

            }

        })
    }

    private fun registerActivityLifecycleCallbacks() {

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {


            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }


    private fun hasAgreedPrivacy(): Boolean {
        return getSharedPreferences("app", MODE_PRIVATE).getBoolean("privacy_agreed", false)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

}