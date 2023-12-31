plugins {
    id("recco.android.library")
    id("recco.android.hilt")
    id("recco.android.spotless")
}

android {
    namespace = "com.recco.internal.core.repository"
}

dependencies {
    implementation(project(":api:model"))
    implementation(project(":internal:core:base"))
    implementation(project(":internal:core:persistence"))
    implementation(project(":internal:core:model"))
    implementation(project(":internal:core:openapi"))
    implementation(project(":internal:core:network"))
    implementation(project(":internal:core:logger"))
}
