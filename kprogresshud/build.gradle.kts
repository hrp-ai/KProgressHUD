import com.android.build.gradle.internal.api.DefaultAndroidSourceDirectorySet

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.kaopiz.kprogresshud"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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


    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }


}

dependencies {
    compileOnly(libs.androidx.core.ktx)
    compileOnly(libs.androidx.activity)
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.kaopiz"
                artifactId = "kprogresshud"
                version = "1.2.0-fixed"
                pom {
                    name.set("KProgressHUD")
                    description.set("Android Progress HUD library with lifecycle-aware management")
                }

                from(components["release"])




            }
        }

        repositories {
            maven {
                url =
                    uri("https://packages.aliyun.com/60852e030f167f18d1971cf8/maven/2098847-release-o6ztzb")
                credentials {
                    username = "6087779f8e4139b4d78024d2"
                    password = "0rC7KI71hB-a"
                }
            }
        }

    }
}