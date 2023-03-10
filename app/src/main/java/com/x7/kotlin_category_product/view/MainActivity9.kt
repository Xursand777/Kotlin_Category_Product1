package com.x7.kotlin_category_product.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.x7.kotlin_category_product.R
import com.x7.kotlin_category_product.databinding.ActivityMain9Binding
import com.x7.kotlin_category_product.model.OrderModel
import com.x7.kotlin_category_product.utilits.Constants.ADMINORDER
import com.x7.kotlin_category_product.view.adapters.OrderAdapter
import com.x7.kotlin_category_product.view.adapters.OrderAdapterAdmin

class MainActivity9 : AppCompatActivity() {
    lateinit var binding: ActivityMain9Binding
    lateinit var databaseReference: DatabaseReference
    var arrayList=ArrayList<OrderModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain9Binding.inflate(layoutInflater)
        setContentView(binding.root)

         databaseReference=FirebaseDatabase.getInstance().getReference().child(ADMINORDER)

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val orderModel=datasnapshot.getValue(OrderModel::class.java)
                    arrayList.add(orderModel!!)
                }
                binding.recyclerviewadminorder.layoutManager=LinearLayoutManager(this@MainActivity9)
                val orderAdapter=OrderAdapterAdmin(this@MainActivity9,arrayList)
                binding.recyclerviewadminorder.adapter=orderAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}