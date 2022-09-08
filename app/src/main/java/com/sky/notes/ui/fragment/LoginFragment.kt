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
import com.sky.notes.databinding.FragmentLoginBinding
import com.sky.notes.model.UserRequest
import com.sky.notes.ui.viewmodels.AuthViewModel
import com.sky.notes.utils.NetworkResult
import com.sky.notes.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first){
                authViewModel.loginUser(getUserRequest())
           } else {
                binding.txtError.text = validationResult.second
            }
        }
        binding.btnSignUp.setOnClickListener{
            findNavController().popBackStack()
        }
        bindObserver()
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val passWord = binding.txtPassword.text.toString()
        return UserRequest(emailAddress,passWord,"")
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateUserCredentials(userRequest.email,
            userRequest.password,
            userRequest.username,true)
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveDta.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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
        _binding=null
    }

}