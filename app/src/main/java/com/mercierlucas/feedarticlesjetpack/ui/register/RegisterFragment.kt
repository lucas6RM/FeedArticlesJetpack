package com.mercierlucas.feedarticlesjetpack.ui.register

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
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefs
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.RegisterDto
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentLoginBinding
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private val registerViewModel: RegisterViewModel by viewModels()
    private var _binding : FragmentRegisterBinding? = null
    private val binding : FragmentRegisterBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var login : String
    private lateinit var password : String
    private lateinit var confirmedPassword : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        registerViewModel.apply {
            messageFromRegisterResponse.observe(viewLifecycleOwner){
                when(it){
                     "5"  -> context?.showToast("login déjà utilisé")
                     "1"  -> context?.showToast("nouveau compte créé")
                     "0"  -> context?.showToast("nouveau compte non créé")
                    "-1"  -> context?.showToast("problème de paramètre")
                    else  -> return@observe
                }
            }
            isResponseCorrect.observe(viewLifecycleOwner){
                if(it){
                    navController.navigate(R.id.action_registerFragment_to_mainFragment)
                }
            }


        }

        binding.btnRegister.setOnClickListener{
            binding.let{
                login = it.etRegisterEnterLogin.text.toString().trim()
                password = it.etRegisterEnterPassword.text.toString().trim()
                confirmedPassword = it.etRegisterConfirmPassword.text.toString().trim()
                if(password == confirmedPassword){
                    if (login.isNotEmpty() && password.isNotEmpty())
                        registerViewModel.registerIn(RegisterDto( login,password))
                    else
                        context?.showToast("login ou mot de passe vide")
                }
                else
                    context?.showToast("erreur sur la confirmation de mot de passe")


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}