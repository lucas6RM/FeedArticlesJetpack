package com.mercierlucas.feedarticlesjetpack.ui.main.articles

import android.content.ContentValues
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercierlucas.feedarticlesjetpack.R
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentMainBinding
import com.mercierlucas.feedarticlesjetpack.ui.main.articles.Destination.*
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_DIVERS
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_SPORT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding : FragmentMainBinding
        get () = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var navDir: NavDirections

    private lateinit var articleAdapter : ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        articleAdapter = ArticleAdapter (onArticleClicked = {articleId,userId ->
            mainViewModel.clickOnAnArticleIsDone(articleId,userId)
        })

        with(binding){

            ibMainLogout.setOnClickListener{
                mainViewModel.clearMySharedPreferences()
                navController.navigate(R.id.action_mainFragment_to_loginFragment)
            }

            // ---- Filtre ----
            rgMain.setOnCheckedChangeListener{_,checkedId ->
                when(checkedId){
                    R.id.rb_sport  -> mainViewModel
                        .refreshfilteredArticles(CATEGORY_SPORT)
                    R.id.rb_manga  -> mainViewModel
                        .refreshfilteredArticles(CATEGORY_MANGA)
                    R.id.rb_divers -> mainViewModel
                        .refreshfilteredArticles(CATEGORY_DIVERS)
                    R.id.rb_tous   -> mainViewModel.refreshArticles()
                    else -> {}
                }
            }

            cbMainFavorites.setOnCheckedChangeListener{_,_ ->
                if(cbMainFavorites.isChecked == true)
                    mainViewModel.setFilterByFavActivated(true)
                else
                    mainViewModel.setFilterByFavActivated(false)
        }


            rvArticles.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = articleAdapter
            }

            ibMainAddArticle.setOnClickListener{
                navController.navigate(R.id.action_mainFragment_to_creaArticleFragment)
            }
        }


        mainViewModel.apply {

            messageFromGetAllPatientsResponse.observe(viewLifecycleOwner){
                when(it){
                    "ok"           -> Log.i(ContentValues.TAG, "Reponse OK")
                    "unauthorized" -> Log.i(ContentValues.TAG, "probleme d'autorisation ")
                    "error_param"  -> Log.i(ContentValues.TAG, "probleme de parametre")
                    else           -> Log.i(ContentValues.TAG, "erreur de connection database")
                }
            }

            articleFilteredList.observe(viewLifecycleOwner){
                articleAdapter.submitList(it)
            }

            articleIdClicked.observe(viewLifecycleOwner)
            { articleId ->
                val destination = mainViewModel.currentFragment.value
                when(destination){
                    FRAG_DETAILS_ARTICLE -> {
                        navDir = MainFragmentDirections
                            .actionMainFragmentToDetailsArticleFragment(articleId)
                        navController.navigate(navDir)
                    }
                    FRAG_EDIT_ARTICLE    -> {
                        navDir = MainFragmentDirections
                            .actionMainFragmentToEditArticleFragment(articleId)
                        navController.navigate(navDir)
                    }
                    null -> {}
                }.also {
                    mainViewModel.resetCurrentFragment()
                }
            }
            articleList.observe(viewLifecycleOwner){
                mainViewModel.resetfilteredArticles()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}