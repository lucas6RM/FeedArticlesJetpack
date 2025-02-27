package com.mercierlucas.feedarticlesjetpack.ui.edit.article

import android.content.ContentValues
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mercierlucas.feedarticles.Utils.showToast
import com.mercierlucas.feedarticlesjetpack.R
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentEditArticleBinding
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_DIVERS
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_SPORT
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditArticleFragment : Fragment() {

    private var _binding: FragmentEditArticleBinding? = null
    private val binding: FragmentEditArticleBinding
        get() = _binding!!

    private val editViewModel: EditArticleViewModel by viewModels()

    private val args: EditArticleFragmentArgs by navArgs()
    private lateinit var navController: NavController

    private lateinit var title: String
    private lateinit var content: String
    private lateinit var imageUrl: String
    private var articleCategory: Int = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        val articleId = args.articleId

        editViewModel.apply {
            getArticleById(articleId)
            messageFromGetArticleResponse.observe(viewLifecycleOwner) {
                when (it) {
                    "ok" -> Log.i(ContentValues.TAG, getString(R.string.response_ok))
                    "unauthorized" -> Log.i(ContentValues.TAG, getString(R.string.unauthorized))
                    "error_param" -> Log.i(ContentValues.TAG, getString(R.string.error_param))
                    else -> Log.i(ContentValues.TAG, getString(R.string.error_connection_db))
                }
            }
            messageFromUpdateArticleResponse.observe(viewLifecycleOwner){
                when(it){
                     "1" -> context?.showToast(getString(R.string.article_modified))
                     "0" -> context?.showToast(getString(R.string.article_not_modified))
                    "-1" -> context?.showToast(getString(R.string.error_param))
                    "-2" -> context?.showToast(getString(R.string.ids_are_different))
                    "-5" -> context?.showToast(getString(R.string.unauthorized))
                    else -> return@observe
                }
            }
            messageFromDeleteArticleResponse.observe(viewLifecycleOwner){
                when(it){
                     "1" -> context?.showToast(getString(R.string.article_deleted))
                     "0" -> context?.showToast(getString(R.string.article_not_deleted))
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

            articleToEdit.observe(viewLifecycleOwner) { articleToEdit ->
                binding.apply {
                    etEditArticleTitle.setText(articleToEdit.titre)
                    etEditArticleDescription.setText(articleToEdit.descriptif)
                    etEditArticleImageUrl.setText(articleToEdit.urlImage)
                    if (articleToEdit.urlImage.isNotEmpty())
                        Picasso
                            .get()
                            .load(articleToEdit.urlImage)
                            .placeholder(R.drawable.feedarticles_logo)
                            .error(R.drawable.feedarticles_logo)
                            .into(ivEditArticle)
                    when (articleToEdit.categorie) {
                        CATEGORY_SPORT  -> rbEditArtSport.isChecked  = true
                        CATEGORY_MANGA  -> rbEditArtManga.isChecked  = true
                        CATEGORY_DIVERS -> rbEditArtDivers.isChecked = true
                    }
                }
            }

            with(binding){
                rgEditArticle.setOnCheckedChangeListener{_,checkedId ->
                    when(checkedId){
                        rbEditArtSport.id  -> articleCategory = CATEGORY_SPORT
                        rbEditArtManga.id  -> articleCategory = CATEGORY_MANGA
                        rbEditArtDivers.id -> articleCategory = CATEGORY_DIVERS
                    }
                }
                btnEditUpdate.setOnClickListener {
                    title = etEditArticleTitle.text.toString().trim()
                    content = etEditArticleDescription.text.toString().trim()
                    imageUrl = etEditArticleImageUrl.text.toString().trim()
                    if (title.isNotEmpty() && content.isNotEmpty())
                        editViewModel.updateArticle(articleId,title,content,imageUrl,articleCategory)
                    else
                        context?.showToast(getString(R.string.empty_required_fields))
                }
                btnEditDelete.setOnClickListener {
                    editViewModel.deleteArticle(articleId)
                }

                etEditArticleImageUrl.setOnFocusChangeListener{ _,_ ->
                    imageUrl = etEditArticleImageUrl.text.toString().trim()
                    if(imageUrl.isNotEmpty())
                        Picasso
                            .get()
                            .load(imageUrl)
                            .placeholder(R.drawable.feedarticles_logo)
                            .error(R.drawable.feedarticles_logo)
                            .into(ivEditArticle)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}