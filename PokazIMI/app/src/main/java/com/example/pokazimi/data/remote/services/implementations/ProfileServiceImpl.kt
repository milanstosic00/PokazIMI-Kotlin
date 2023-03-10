package com.example.pokazimi.data.remote.services.implementations

import com.example.pokazimi.data.remote.HttpRoutes
import com.example.pokazimi.data.remote.dto.ChangeProfilePictureRequest
import com.example.pokazimi.data.remote.dto.ChangeProfilePictureResponse
import com.example.pokazimi.data.remote.dto.User
import com.example.pokazimi.data.remote.services.ProfileService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class ProfileServiceImpl(private val client: HttpClient): ProfileService {
    override suspend fun change(changeRequest: ChangeProfilePictureRequest): ChangeProfilePictureResponse? {
        return try {
            client.submitFormWithBinaryData(
                url = HttpRoutes.CHANGE_PROFILE_PICTURE,
                formData = formData {
                    append("userId", changeRequest.userId)
                    append("profilePicture", changeRequest.profilePicture, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=profilePicture.png")
                    })
                }
            )
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

    override suspend fun getUser(userId: Long): User? {
        return try {
            client.get<User>{
                url(HttpRoutes.GET_USER + "/$userId")
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            null
        }
    }

}