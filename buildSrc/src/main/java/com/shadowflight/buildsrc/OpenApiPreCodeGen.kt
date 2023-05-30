package com.shadowflight.buildsrc

import com.shadowflight.buildsrc.OpenApiCodeGenUtils.PATH_APIS
import com.shadowflight.buildsrc.OpenApiCodeGenUtils.PATH_DATA
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Gradle task to run before generating the code based on the OpenApi schema.
 */
open class OpenApiPreCodeGen : DefaultTask() {

    @TaskAction
    fun openApiPreCodeGen() {
        listOf(PATH_APIS, PATH_DATA)
            .forEach { fileToDelete -> File(fileToDelete).deleteRecursively() }
    }
}
