package com.example.pokazimi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokazimi.R
import com.example.pokazimi.data.remote.dto.LogInResponse
import com.example.pokazimi.data.remote.dto.LoginRequest
import com.example.pokazimi.data.remote.dto.RegistrationRequest
import com.example.pokazimi.data.remote.services.LogInService
import com.example.pokazimi.data.remote.services.RegistrationService
import com.example.pokazimi.dataStore.Storage
import com.example.pokazimi.destinations.MainScreenDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

@Destination
@Composable
fun LoginScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = Storage(context)

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color(0xffff0008))
    }

    var firstName by remember {
        mutableStateOf("")
    }

    var lastName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    var expandedState by remember {
        mutableStateOf(false)
    }

    val isFormValid by derivedStateOf {
        email.isNotBlank() && password.length >= 5
    }



    Scaffold(backgroundColor = MaterialTheme.colors.primary) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top){
            Image(
                painter = painterResource(id = R.drawable.instagram),
                contentDescription = "App Logo",
                modifier = Modifier
                    .weight(1f)
                    .size(188.dp),
                colorFilter = ColorFilter.tint(Color.White),

            )
            Card(
                modifier = Modifier
                    .weight(if (!expandedState) 2f else 5f)
                    .padding(8.dp),
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Text(text = "Welcome Back!", fontWeight = FontWeight.Bold, fontSize = 32.sp)

                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Spacer(modifier = Modifier.weight(1f))
                        if(expandedState) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = firstName,
                                onValueChange = {firstName = it},
                                label = { Text(text = "First Name")},
                                trailingIcon = {
                                    if(firstName.isNotBlank()) {
                                        IconButton(onClick = { firstName = "" }) {
                                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = lastName,
                                onValueChange = {lastName = it},
                                label = { Text(text = "Last Name")},
                                trailingIcon = {
                                    if(lastName.isNotBlank()) {
                                        IconButton(onClick = { lastName = "" }) {
                                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = username,
                                onValueChange = {username = it},
                                label = { Text(text = "Username")},
                                trailingIcon = {
                                    if(username.isNotBlank()) {
                                        IconButton(onClick = { username = "" }) {
                                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = email,
                            onValueChange = {email = it},
                            label = { Text(text = "Email")},
                            trailingIcon = {
                                if(email.isNotBlank()) {
                                    IconButton(onClick = { email = "" }) {
                                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = {password = it},
                            label = { Text(text = "Password")},
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if(isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            //Preradi ovu funkciju
                            onClick = {
                                if(!expandedState)
                                {
                                    val loginResponse = login(email, password)
                                    if(loginResponse != null) {

                                        val path = context.getExternalFilesDir(null)!!.absolutePath
                                        val tempFile = File(path, "tokens.txt")
                                        tempFile.writeText(loginResponse.refreshToken + "\n" + loginResponse.accessToken)
                                        scope.launch { dataStore.saveAccessToken(loginResponse.accessToken) }
                                        scope.launch { dataStore.saveRefreshToken(loginResponse.refreshToken) }
                                        navigator.navigate(MainScreenDestination)

                                    }
                                    else {
                                        //navigator.navigate(MainScreenDestination)
                                    }
                                }
                                else
                                {
                                    if(register(username, password, firstName, lastName, email)) {
                                        username = ""
                                        password = ""
                                        firstName = ""
                                        lastName = ""
                                        expandedState = false
                                    }

                                }
                            },
                            enabled = isFormValid,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            if(!expandedState){
                                Text(text = "Log In")
                            }
                            else {
                                Text(text = "Register")
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = { expandedState = !expandedState }) {
                                if(!expandedState){
                                    Text(text = "Sign Up")
                                }
                                else {
                                    Text(text = "Sign In")
                                }
                            }
                            TextButton(onClick = {}) {
                                Text(text = "Forgot Password?", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun register(username: String, password: String, firstName: String, lastName: String, email: String): Boolean {
    val service = RegistrationService.create()

    if(runBlocking { service.registration(RegistrationRequest(firstName, lastName, username, email, password)) }  == null)
    {
        return false
    }
    return true
}

fun login(username: String, password: String) : LogInResponse? {

    val service = LogInService.create()
    val response = runBlocking { service.login(LoginRequest(username, password)) } ?: return null

    println("access token je: asdasdasd " + response.accessToken)
    return response
}

/*
@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen()
}
*/