plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.mvvmdemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mvvmdemo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding =true
        dataBinding  = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.utilcodex)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.com.squareup.retrofit2.converter.gson8)
    implementation (libs.logging.interceptor)
    // 协程
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
// RxJava
    implementation (libs.rxjava3.rxjava)
    implementation (libs.rxjava3.rxandroid)
    implementation (libs.squareup.adapter.rxjava3)

// Lifecycle & ViewModel
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
//    爱奇艺崩溃 XCrash
    implementation (libs.xcrash.android.lib)
//    glide
    implementation (libs.github.glide)
// 拼音
    implementation(libs.pinyin4j)
//    mob广告
    implementation (libs.androidx.legacy.support.v4)
    implementation (libs.androidx.fragment.fragment.ktx2)
    implementation (libs.banner)
    implementation (libs.androidx.core.splashscreen)
//    刷新控件
    implementation (libs.github.refresh.layout.kernel)
    implementation (libs.io.github.scwang90.refresh.header.classics)
    implementation (libs.io.github.scwang90.refresh.footer.classics)
//    65535
    implementation(libs.androidx.multidex)
//    MMKV
    implementation (libs.mmkv)
//    eventBusBUS
    implementation(libs.eventbus)
//    适配器
    implementation (libs.baserecyclerviewadapterhelper)
//    内存泄露检测
    implementation (libs.leakcanary.android)

}