package com.mercierlucas.feedarticlesjetpack.ui.main.articles

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mercierlucas.feedarticlesjetpack.R
import com.mercierlucas.feedarticlesjetpack.data.entity.dtos.Article
import com.mercierlucas.feedarticlesjetpack.databinding.ArticleItemViewBinding
import com.mercierlucas.feedarticlesjetpack.utils.formatApiDate
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat


class ArticleAdapter : ListAdapter<Article, ArticleViewHolder>(ArticleDiffCallback){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item_view,parent,false)

        return ArticleViewHolder(itemView)
    }


    @SuppressLint("UseCompatLoadingForDrawables", "PrivateResource")
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)

        val sportCard = holder.itemView.resources.getDrawable(R.drawable.card_sport)
        val mangaCard = holder.itemView.resources.getDrawable(R.drawable.card_manga)
        val othersCard = holder.itemView.resources.getDrawable(R.drawable.card_others)
        val blankCard = holder.itemView.resources.getDrawable(R.drawable.card_blank)

        holder.apply {
            with(binding){
                ivItemView.let {
                    Picasso.get()
                        .load(article.urlImage.ifEmpty { "image vide" })
                        .placeholder(R.drawable.feedarticles_logo)
                        .error(com.google.android.material.R.drawable.mtrl_ic_error)
                        .into(this.ivItemView)
                }
                tvItemViewArticleTitle.text = article.titre
                tvItemViewArticleDesc.text = article.descriptif
                tvItemViewArticleDate.text = formatApiDate(article.createdAt)

                when(article.categorie){
                    1    -> cvItemView.background = sportCard
                    2    -> cvItemView.background = mangaCard
                    3    -> cvItemView.background = othersCard
                    else -> clItemView.background = blankCard
                }
            }
        }
    }


}

object ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
class ArticleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val binding = ArticleItemViewBinding.bind(itemView)

}