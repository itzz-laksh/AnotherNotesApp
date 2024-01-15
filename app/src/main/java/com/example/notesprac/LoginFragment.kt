package com.example.notesprac

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesprac.databinding.FragmentLoginBinding
import com.example.notesprac.models.UserRequest
import com.example.notesprac.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val validatorResult = validInputUser()
            if(validatorResult.first){
                authViewModel.loginUser(getUserRequest())
            } else {
                binding.txtError.text = validatorResult.second
            }


        }

        binding.btnSignUp.setOnClickListener {
            //authViewModel.registerUser(UserRequest("xyz@gmail.com","114454124", "XYZ1414"))
            findNavController().popBackStack()
        }

        bindObserver()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun getUserRequest() : UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val userName = ""
        val password = binding.txtPassword.text.toString()
        return UserRequest(emailAddress,password,userName)
    }

    private fun validInputUser(): Pair<Boolean,String> {
        val userRequest = getUserRequest()
        return authViewModel.validCredentials(userRequest.email,userRequest.username,userRequest.password, true)
    }

}