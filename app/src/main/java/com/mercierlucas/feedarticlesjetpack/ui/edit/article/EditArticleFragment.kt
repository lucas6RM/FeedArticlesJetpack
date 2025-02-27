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
                    "ok" -> Log.i(ContentValues.TAG, "Reponse OK")
                    "unauthorized" -> Log.i(ContentValues.TAG, "probleme d'autorisation ")
                    "error_param" -> Log.i(ContentValues.TAG, "probleme de parametre")
                    else -> Log.i(ContentValues.TAG, "erreur de connection database")
                }
            }
            messageFromUpdateArticleResponse.observe(viewLifecycleOwner){
                when(it){
                     "1" -> context?.showToast("article modifié")
                     "0" -> context?.showToast("pas de modification")
                    "-1" -> context?.showToast("probleme de parametre")
                    "-2" -> context?.showToast("les ids (path et dto) sont différents")
                    "-5" -> context?.showToast("modification non autorisée")
                    else -> return@observe
                }
            }
            messageFromDeleteArticleResponse.observe(viewLifecycleOwner){
                when(it){
                     "1" -> context?.showToast("article supprimé")
                     "0" -> context?.showToast("pas de suppression")
                    "-1" -> context?.showToast("probleme de parametre")
                    "-5" -> context?.showToast("suppression non autorisée")
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
                        context?.showToast("Champs manquant à remplir")
                }
                btnEditDelete.setOnClickListener {
                    editViewModel.deleteArticle(articleId)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}