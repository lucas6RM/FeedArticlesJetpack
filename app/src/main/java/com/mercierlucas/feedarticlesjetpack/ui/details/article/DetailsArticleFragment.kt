package com.mercierlucas.feedarticlesjetpack.ui.details.article

import android.content.ContentValues
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mercierlucas.feedarticles.Utils.showToast
import com.mercierlucas.feedarticlesjetpack.R
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentDetailsArticleBinding
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_DIVERS
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_SPORT
import com.mercierlucas.feedarticlesjetpack.utils.formatApiDate
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsArticleFragment : Fragment() {

    private var _binding : FragmentDetailsArticleBinding? = null
    private val binding : FragmentDetailsArticleBinding
        get() = _binding!!

    private val detailViewModel: DetailsArticleViewModel by viewModels()

    private val args : DetailsArticleFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsArticleBinding
            .inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        val articleId = args.articleId

        detailViewModel.apply {
            getArticleById(articleId)
            messageFromGetArticleResponse.observe(viewLifecycleOwner){
                when(it){
                    "ok" -> Log.i(ContentValues.TAG, getString(R.string.response_ok))
                    "unauthorized" -> Log.i(ContentValues.TAG, getString(R.string.unauthorized))
                    "error_param" -> Log.i(ContentValues.TAG, getString(R.string.error_param))
                    else -> Log.i(ContentValues.TAG, getString(R.string.error_connection_db))
                }
            }
            messageFromAddFavoriteResponse.observe(viewLifecycleOwner){
                when(it){
                     "1" -> context?.showToast(getString(R.string.status_favorite_changed))
                     "0" -> context?.showToast(getString(R.string.status_favorite_not_changed))
                    "-1" -> context?.showToast(getString(R.string.error_param))
                    "-5" -> context?.showToast(getString(R.string.unauthorized))
                }
            }

            articleToDisplay.observe(viewLifecycleOwner){ article ->
                binding.apply {
                    tvDetailsArticleTitle.text = article.titre
                    tvDetailsArticleDate.text = getString(R.string.created_on,formatApiDate(article.createdAt))
                    when (article.categorie) {
                        CATEGORY_SPORT -> tvDetailsArticleCategory.text = getString(R.string.category_s,getString(R.string.sport))
                        CATEGORY_MANGA -> tvDetailsArticleCategory.text = getString(R.string.category_s,getString(R.string.manga))
                        CATEGORY_DIVERS -> tvDetailsArticleCategory.text = getString(R.string.category_s,getString(R.string.others))
                    }
                    tvDetailsArticleCategory.text
                    if (article.urlImage.isNotEmpty())
                        Picasso
                            .get()
                            .load(article.urlImage)
                            .placeholder(R.drawable.feedarticles_logo)
                            .error(R.drawable.feedarticles_logo)
                            .into(ivDetailsArticle)
                    tvDetailsArticleDescription.text = article.descriptif
                    if (article.isFav == 1)
                        cbDetailsFavorites.isChecked = true
                    else
                        cbDetailsFavorites.isChecked = false
                }
            }

            with(binding) {
                cbDetailsFavorites.setOnClickListener {
                    lifecycleScope.launch {
                    cbDetailsFavorites.isClickable = false
                    detailViewModel.toggleAddToFavorites(articleId)
                    delay(3000)
                    cbDetailsFavorites.isClickable = true
                    }
                }
                btnDetailsBack.setOnClickListener {
                    navController.popBackStack()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}