package com.sky.notes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sky.notes.R
import com.sky.notes.databinding.FragmentRegisterBinding
import com.sky.notes.model.UserRequest
import com.sky.notes.ui.viewmodels.AuthViewModel
import com.sky.notes.utils.NetworkResult
import com.sky.notes.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _registerBinding: FragmentRegisterBinding? = null
    private val binding get() = _registerBinding!!
    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>()
   @Inject lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _registerBinding = FragmentRegisterBinding.inflate(inflater, container, false)

        if (tokenManager.getToken()!=null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val validationResult=validateUserInput()
            if (!validationResult.first){
                authViewModel.registerUser(getUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        bindObserver()


    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val passWord = binding.txtPassword.text.toString()
        val userName = binding.txtUsername.text.toString()
        return UserRequest(emailAddress,passWord,userName)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateUserCredentials(userRequest.email,
            userRequest.password,
            userRequest.username,false)
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveDta.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                  tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _registerBinding = null
    }

}