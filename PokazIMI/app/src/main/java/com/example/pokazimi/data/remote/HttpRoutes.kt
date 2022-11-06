package com.example.pokazimi.data.remote

object HttpRoutes {
    private const val BASE_URL = "http://10.0.2.2:8080"

    private const val TEST_URL = "https://jsonplaceholder.typicode.com"
    const val POSTS = "$TEST_URL/posts"

    const val REFRESH = "$BASE_URL/auth/refresh"
    const val LOGIN = "$BASE_URL/auth/login"
    const val REGISTER = "$BASE_URL/auth/register"

    const val SAVE_POST = "$BASE_URL/post/save"
}