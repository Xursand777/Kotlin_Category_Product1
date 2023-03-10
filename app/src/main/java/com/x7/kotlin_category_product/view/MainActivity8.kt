package com.x7.kotlin_category_product.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.x7.kotlin_category_product.R
import com.x7.kotlin_category_product.databinding.ActivityMain8Binding
import com.x7.kotlin_category_product.model.OrderModel
import com.x7.kotlin_category_product.utilits.Constants.HISTORY
import com.x7.kotlin_category_product.view.adapters.OrderAdapter

class MainActivity8 : AppCompatActivity() {
    lateinit var binding: ActivityMain8Binding
    lateinit var databaseReference: DatabaseReference
    var arrayList=ArrayList<OrderModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain8Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var phone=intent.getStringExtra("phone")
        databaseReference=FirebaseDatabase.getInstance().getReference().child(HISTORY).child(phone!!)

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (datasnapshot:DataSnapshot in snapshot.children){
                    val orderModel=datasnapshot.getValue(OrderModel::class.java)
                    arrayList.add(orderModel!!)
                }
                binding.recyclerviewhistory.layoutManager=LinearLayoutManager(this@MainActivity8)
                val orderAdapter=OrderAdapter(this@MainActivity8,arrayList)
                binding.recyclerviewhistory.adapter=orderAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }
}