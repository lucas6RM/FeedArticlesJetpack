package com.mercierlucas.feedarticlesjetpack.ui.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mercierlucas.feedarticles.Utils.showToast
import com.mercierlucas.feedarticlesjetpack.R
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {


    private val loginViewModel: LoginViewModel by viewModels()

    private var _binding : FragmentLoginBinding? = null
    private val binding : FragmentLoginBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var login : String
    private lateinit var password : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        loginViewModel.apply {
            messageFromLoginResponse.observe(viewLifecycleOwner){
                when(it){
                     "5" -> context?.showToast(getString(R.string.token_security_problem))
                     "1" -> context?.showToast(getString(R.string.ok_new_token_delivered))
                     "0" -> context?.showToast(getString(R.string.user_unknown))
                    "-1" -> context?.showToast(getString(R.string.error_param))
                    else -> return@observe
                }
            }
            isResponseCorrect.observe(viewLifecycleOwner){
                if(it){
                    navController.navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
        }

        with(binding){
            btnLogin.setOnClickListener{
                    login = etLoginEnterLogin.text.toString().trim()
                    password = etLoginEnterPassword.text.toString().trim()
                    if(login.isNotEmpty() && password.isNotEmpty())
                        loginViewModel.signIn(login,password)
                    else
                        context?.showToast(getString(R.string.login_or_password_empty))
            }
            tvLoginGoRegister.setOnClickListener{
                navController.navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}