package com.mercierlucas.feedarticlesjetpack.ui.create.article

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercierlucas.feedarticlesjetpack.R

class CreaArticleFragment : Fragment() {

    companion object {
        fun newInstance() = CreaArticleFragment()
    }

    private val viewModel: CreaArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_crea_article, container, false)
    }
}