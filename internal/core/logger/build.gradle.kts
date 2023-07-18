plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.core.logger"
}

dependencies {
    implementation(project(":api:model"))
}
