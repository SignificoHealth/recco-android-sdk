package com.shadowflight.network.http

enum class ApiEndpoint(
    val baseUrl: String
) {
    PROD(
        baseUrl = "https://api.sf-dev.significo.dev/"
    ),
    STAGING(
        baseUrl = "https://api.sf-dev.significo.dev/"
    ),
    LOCAL(
        baseUrl = "http://10.0.2.2:8080/",
    );
}
