package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.ChangeProfilePictureRequest
import com.example.pokazimi.data.remote.dto.ChangeProfilePictureResponse
import com.example.pokazimi.data.remote.dto.LogInResponse
import com.example.pokazimi.data.remote.dto.LoginRequest
import com.example.pokazimi.data.remote.services.ChangeProfilePictureService
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ChangeProfilePictureServiceImpl(private val client: HttpClient): ChangeProfilePictureService {
    override suspend fun change(changeRequest: ChangeProfilePictureRequest): ChangeProfilePictureResponse? {
        return try {
            client.submitFormWithBinaryData(
                url = HttpRoutes.CHANGE_PROFILE_PICTURE,
                formData = formData {
                    append("userId", changeRequest.userId)
                    append("image", changeRequest.profilePicture, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=image.png")
                    })
                }
            )
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

}