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
                     "5" -> context?.showToast("authentifié mais token inchangé")
                     "1" -> context?.showToast("authentifié avec un nouveau token")
                     "0" -> context?.showToast("user inconnu (mauvais login/mdp)")
                    "-1" -> context?.showToast("problème de paramètre")
                    else -> return@observe
                }
            }
            isResponseCorrect.observe(viewLifecycleOwner){
                if(it){
                    navController.navigate(R.id.action_loginFragment_to_mainFragment)
                    loginViewModel.resetIsResponseCorrect()
                }
            }
        }

        binding.btnLogin.setOnClickListener{
            binding.let {
                login = it.etLoginEnterLogin.text.toString().trim()
                password = it.etLoginEnterPassword.text.toString().trim()
                if(login.isNotEmpty() && password.isNotEmpty())
                    loginViewModel.signIn(login,password)
                else
                    context?.showToast("login ou mot de passe vide")
            }
        }

        binding.tvLoginGoRegister.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}