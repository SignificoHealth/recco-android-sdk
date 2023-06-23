plugins {
    id("recco.android.library")
    id("recco.android.hilt")
}

android {
    namespace = "com.recco.internal.core.logger"
}

dependencies {
    implementation(project(":api:model"))
}