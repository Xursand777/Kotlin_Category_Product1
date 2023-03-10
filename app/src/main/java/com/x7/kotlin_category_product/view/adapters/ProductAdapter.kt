package com.x7.kotlin_category_product.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.x7.kotlin_category_product.databinding.RecyclerviewItem2Binding
import com.x7.kotlin_category_product.model.ProductModel
import com.x7.kotlin_category_product.view.MainActivity4


class ProductAdapter constructor(
    val context: Context,
    var arrayList: ArrayList<ProductModel>,
):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view=RecyclerviewItem2Binding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
       holder.binding.apply {
           textviewproduct.text=arrayList.get(position).name
           Glide.with(context).load(arrayList.get(position).imguri).centerCrop().into(imageviewproduct)
           linearlay1.setOnClickListener {
               var intent=Intent(context,MainActivity4::class.java)
               intent.putExtra("product",arrayList.get(position))
               context.startActivity(intent)
           }
       }
    }

    override fun getItemCount(): Int =arrayList.size
    fun filterList(filteredList: ArrayList<ProductModel>) {
        arrayList = filteredList
        notifyDataSetChanged()
    }
    class ProductViewHolder(val binding: RecyclerviewItem2Binding):RecyclerView.ViewHolder(binding.root)
}


