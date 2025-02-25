package com.mercierlucas.feedarticlesjetpack.ui.create.article

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
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentCreaArticleBinding
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_DIVERS
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_SPORT
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreaArticleFragment : Fragment() {

    private var _binding : FragmentCreaArticleBinding? = null
    private val binding : FragmentCreaArticleBinding
        get() = _binding!!


    private val creaArticleViewModel: CreaArticleViewModel by viewModels()
    private lateinit var title : String
    private lateinit var content : String
    private lateinit var imageUrl : String
    private var articleCategory: Int = CATEGORY_DIVERS
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreaArticleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        creaArticleViewModel.apply {
            errorMessage.observe(viewLifecycleOwner){
                when(it){
                     "1" -> context?.showToast("article cree")
                     "0" -> context?.showToast("pas de creation")
                    "-1" -> context?.showToast("probleme de parametre")
                    "-5" -> context?.showToast("creation non autorisée")
                    else -> return@observe
                }
            }
            isResponseCorrect.observe(viewLifecycleOwner){
                if(it){
                    navController.navigate(R.id.action_creaArticleFragment_to_mainFragment)
                    creaArticleViewModel.resetIsResponseCorrect()
                }
            }
        }

        with(binding) {
            rgCreaArticle.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb_crea_art_sport -> articleCategory = CATEGORY_SPORT
                    R.id.rb_crea_art_manga -> articleCategory = CATEGORY_MANGA
                    R.id.rb_crea_art_divers -> articleCategory = CATEGORY_DIVERS
                    else -> {}
                }
            }
            btnCreaRegister.setOnClickListener {
                title = etCreaArticleTitle.text.toString().trim()
                content = etCreaArticleDescription.text.toString().trim()
                imageUrl = etCreaArticleImageUrl.text.toString().trim()

                if (title.isNotEmpty() && content.isNotEmpty())
                    creaArticleViewModel.addNewArticle(
                        title,
                        content,
                        imageUrl,
                        articleCategory
                    )
                else
                    context?.showToast("Champs manquant à remplir")
            }
            etCreaArticleImageUrl.setOnFocusChangeListener{_,_ ->
                imageUrl = etCreaArticleImageUrl.text.toString().trim()
                if(imageUrl.isNotEmpty())
                    Picasso
                        .get()
                        .load(imageUrl)
                        .placeholder(R.drawable.feedarticles_logo)
                        .error(com.google.android.material.R.drawable.mtrl_ic_error)
                        .into(ivCreaArticle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}