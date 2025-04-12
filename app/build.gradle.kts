import org.gradle.process.internal.DefaultExecOperations
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

fun getVersionCode(): Int {
    val output = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-list", "--count", "--first-parent", "HEAD")
        standardOutput = output
    }
    return output.toByteArray().toString(Charset.defaultCharset()).trim().toInt()
}

fun getVersionName(): String {
    val output = ByteArrayOutputStream()
    exec {
        commandLine("git", "describe", "--tags", "--match", "v[0-9]*")
        standardOutput = output
    }
    return output.toByteArray().toString(Charset.defaultCharset())
        .trim()
        .trimStart('v') // 去掉 v 前缀
}

android {
    namespace = "ink.duo3.caelum"
    compileSdk = 35

    defaultConfig {
        applicationId = "ink.duo3.caelum"
        minSdk = 24
        targetSdk = 35
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val props = Properties().apply { load(rootProject.file("local.properties").inputStream()) }
            buildConfigField("String", "API_BASE_URL", props.getProperty("app.apiBaseUrl.prod"))
        }
        debug {
            val props = Properties().apply { load(rootProject.file("local.properties").inputStream()) }
            buildConfigField("String", "API_BASE_URL", props.getProperty("app.apiBaseUrl.dev"))
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.material)
    implementation(libs.androidx.graphics.shapes)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.accompanist.permissions)
    implementation(libs.material.motion)
    implementation(libs.haze)
    implementation(libs.haze.materials)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)

    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.encoding)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.kotlinx.datetime)

    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}