package com.mercierlucas.feedarticlesjetpack.ui.main.articles

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercierlucas.feedarticlesjetpack.R
import com.mercierlucas.feedarticlesjetpack.databinding.FragmentMainBinding
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


        // LOGOUT
        binding.ibMainLogout.setOnClickListener{
            mainViewModel.clearMySharedPreferences()
            navController.navigate(R.id.action_mainFragment_to_loginFragment)
        }

        // FILTER

        binding.rgMain.setOnCheckedChangeListener{_,checkedId ->
            when(checkedId){
                R.id.rb_sport -> mainViewModel
                    .refreshfilteredArticles(CATEGORY_SPORT)
                R.id.rb_manga -> mainViewModel
                    .refreshfilteredArticles(CATEGORY_MANGA)
                R.id.rb_divers -> mainViewModel
                    .refreshfilteredArticles(CATEGORY_DIVERS)
                R.id.rb_tous -> mainViewModel.refreshArticles()
                else -> {}
            }
        }

        // RECYCLER VIEW
        articleAdapter = ArticleAdapter()
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = articleAdapter
        }

        mainViewModel.articleList.observe(viewLifecycleOwner){
            mainViewModel.resetfilteredArticles()
        }

        mainViewModel.articleFilteredList.observe(viewLifecycleOwner){
            articleAdapter.submitList(it)
        }


        // ADD ARTICLE
        binding.ibMainAddArticle.setOnClickListener{
            navController.navigate(R.id.action_mainFragment_to_creaArticleFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}