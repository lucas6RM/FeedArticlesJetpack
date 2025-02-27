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
            messageFromAddNewArticleResponse.observe(viewLifecycleOwner){
                when(it){
                     "1" -> context?.showToast(getString(R.string.article_created))
                     "0" -> context?.showToast(getString(R.string.article_not_created))
                    "-1" -> context?.showToast(getString(R.string.error_param))
                    "-5" -> context?.showToast(getString(R.string.unauthorized))
                    else -> return@observe
                }
            }
            isResponseCorrect.observe(viewLifecycleOwner){
                if(it){
                    navController.popBackStack()
                }
            }
        }

        with(binding) {
            rgCreaArticle.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbCreaArtSport.id  -> articleCategory = CATEGORY_SPORT
                    rbCreaArtManga.id  -> articleCategory = CATEGORY_MANGA
                    rbCreaArtDivers.id -> articleCategory = CATEGORY_DIVERS
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
                    context?.showToast(getString(R.string.empty_required_fields))
            }
            etCreaArticleImageUrl.setOnFocusChangeListener{ _,_ ->
                imageUrl = etCreaArticleImageUrl.text.toString().trim()
                if(imageUrl.isNotEmpty())
                    Picasso
                        .get()
                        .load(imageUrl)
                        .placeholder(R.drawable.feedarticles_logo)
                        .error(R.drawable.feedarticles_logo)
                        .into(ivCreaArticle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}