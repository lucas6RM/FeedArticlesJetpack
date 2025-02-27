package com.mercierlucas.feedarticlesjetpack.ui.main.articles

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
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercierlucas.feedarticlesjetpack.R
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentMainBinding
import com.mercierlucas.feedarticlesjetpack.ui.main.articles.Destination.*
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_DIVERS
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_SPORT
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_TOUS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

            swiperefresh.setOnRefreshListener {
                refreshMainMenu()
                swiperefresh.isRefreshing = false
            }

            ibMainLogout.setOnClickListener{
                mainViewModel.clearMySharedPreferences()
                navController.navigate(R.id.action_mainFragment_to_loginFragment)
            }


            rgMain.setOnCheckedChangeListener{ _,checkedId ->
                checkCurrentCategory(checkedId)
            }

            cbMainFavorites.setOnCheckedChangeListener{ _,_ ->
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

            currentCategory.observe(viewLifecycleOwner){
                mainViewModel.refreshfilters()
            }

            isFilterFavActivated.observe(viewLifecycleOwner){
                mainViewModel.refreshfilters()
            }

            articleList.observe(viewLifecycleOwner){
                mainViewModel.refreshfilters()
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
        }
    }

    private fun checkCurrentCategory(checkedId: Int) {
        with(binding) {
            when (checkedId) {
                rbSport.id -> mainViewModel.setCurrentCategory(CATEGORY_SPORT)
                rbManga.id -> mainViewModel.setCurrentCategory(CATEGORY_MANGA)
                rbDivers.id -> mainViewModel.setCurrentCategory(CATEGORY_DIVERS)
                rbTous.id -> mainViewModel.setCurrentCategory(CATEGORY_TOUS)
                else -> {}
            }
        }
    }

    private fun refreshMainMenu() {
        mainViewModel.refreshArticles()
        with(binding){
            rgMain.check(rbTous.id)
        }
    }


    override fun onResume() {
        super.onResume()
        mainViewModel.refreshArticles()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}