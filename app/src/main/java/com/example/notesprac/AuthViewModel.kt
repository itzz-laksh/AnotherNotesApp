package com.example.notesprac

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesprac.models.UserRequest
import com.example.notesprac.models.UserResponse
import com.example.notesprac.repository.UserRepository
import com.example.notesprac.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
    get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validCredentials(emailAddress: String, userName: String, password: String, isLogin: Boolean) : Pair<Boolean, String> {
        var result = Pair(true, "")
        if((!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(password) || TextUtils.isEmpty(emailAddress)){
            result = Pair(false, "Please provide credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            result = Pair(false,"Please provide valid email")
        } else if (password.length < 6) {
            result = Pair(false,"Password length should be greater than 5")
        }
        return result
    }
}