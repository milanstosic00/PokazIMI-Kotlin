package com.example.pokazimi.data.remote

object HttpRoutes {
    private const val BASE_URL = "localhost:xxxxx"

    private const val TEST_URL = "https://jsonplaceholder.typicode.com"
    const val POSTS = "$TEST_URL/posts"

    const val LOGIN = "$BASE_URL/login"
    const val REGISTER = "$BASE_URL/register"
}