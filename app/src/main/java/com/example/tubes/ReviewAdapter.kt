package com.example.tubes

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_reservasi_adapter.view.*
import kotlinx.android.synthetic.main.activity_reservasi_adapter.view.icon_delete
import kotlinx.android.synthetic.main.activity_reservasi_adapter.view.icon_edit
import kotlinx.android.synthetic.main.activity_review_adapter.view.*

class ReviewAdapter (private val reviews: ArrayList<com.example.tubes.models.Review>, private val listener: OnAdapterListener) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ReviewViewHolder {
        return ReviewViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_review_adapter,parent, false)
        )
    }
    override fun onBindViewHolder(holder: ReviewViewHolder, position:
    Int) {
        val Review = reviews[position]
        holder.view.text_review.text = Review.review
        holder.view.text_saran.text = Review.saran

        holder.view.text_review.setOnClickListener{
            listener.onClick(Review)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(Review)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(Review)
        }
    }
    override fun getItemCount() = reviews.size
    inner class ReviewViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: Array<com.example.tubes.models.Review>){
        reviews.clear()
        reviews.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(Review: com.example.tubes.models.Review)
        fun onUpdate(Review: com.example.tubes.models.Review)
        fun onDelete(Review: com.example.tubes.models.Review)
    }
}