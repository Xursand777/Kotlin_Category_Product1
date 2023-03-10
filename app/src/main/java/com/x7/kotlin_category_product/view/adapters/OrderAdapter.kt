package com.x7.kotlin_category_product.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.x7.kotlin_category_product.databinding.RecyclerviewItemOrderBinding
import com.x7.kotlin_category_product.model.OrderModel

class OrderAdapter constructor(
    val context: Context,
    val arrayList: ArrayList<OrderModel>
):RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view=RecyclerviewItemOrderBinding.inflate(LayoutInflater.from(context),parent,false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
     holder.binding.textvieworderread.text="${arrayList.get(position).datatime}\n${arrayList.get(position).orders}"
    }
    override fun getItemCount(): Int =arrayList.size



    class OrderViewHolder(val binding: RecyclerviewItemOrderBinding):RecyclerView.ViewHolder(binding.root)
}