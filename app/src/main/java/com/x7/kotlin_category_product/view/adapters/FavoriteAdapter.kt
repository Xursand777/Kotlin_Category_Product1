package com.x7.kotlin_category_product.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.x7.kotlin_category_product.databinding.RecyclerviewItemSavedBinding
import com.x7.kotlin_category_product.model.ProductModel
import com.x7.kotlin_category_product.utilits.Constants.ORDERS
import com.x7.kotlin_category_product.utilits.Constants.USERNAME

class FavoriteAdapter constructor(
    val context: Context,
    var arrayList: ArrayList<ProductModel>
):RecyclerView.Adapter<FavoriteAdapter.SavedViewHolder>() {

    val databaseReference:DatabaseReference=FirebaseDatabase.getInstance().getReference().child(ORDERS).child(USERNAME)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val view=RecyclerviewItemSavedBinding.inflate(LayoutInflater.from(context),parent,false)
        return SavedViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
     holder.binding.textviewfavoriteorder.text="${arrayList.get(position).name}\n${arrayList.get(position).price} UZS"
        holder.binding.imageviewfavoritedelete.setOnClickListener {
        databaseReference.child(arrayList.get(position).pushkey.toString()).removeValue()
        }
    }
    override fun getItemCount(): Int =arrayList.size



    class SavedViewHolder(val binding: RecyclerviewItemSavedBinding):RecyclerView.ViewHolder(binding.root)
}