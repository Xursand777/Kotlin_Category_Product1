package com.x7.kotlin_category_product.view

import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.x7.kotlin_category_product.R
import com.x7.kotlin_category_product.databinding.ActivityMain7Binding
import com.x7.kotlin_category_product.view.adapters.FavoriteAdapter
import com.x7.kotlin_category_product.viewmodel.ProductViewModel

class MainActivity7 : AppCompatActivity() {
    lateinit var binding: ActivityMain7Binding
    lateinit var productViewModel: ProductViewModel
    lateinit var favoriteAdapter: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain7Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent=intent
        title=intent.getStringExtra("username")
        var username=intent.getStringExtra("username")
        var surname=intent.getStringExtra("surname")
        var phone=intent.getStringExtra("phone")
        var adress=intent.getStringExtra("adress")

        productViewModel=ViewModelProvider(this@MainActivity7).get(ProductViewModel::class.java)

        productViewModel.readallorderss(username!!).observe(this@MainActivity7,{
            binding.textvieworderreadac7.text=""
            var totalcost=0
            for (i in 0 until it.size){
                binding.textvieworderreadac7.append("${it.get(i).name} ${it.get(i).price} \n")
                totalcost=totalcost+it.get(i).price!!.toInt()
            }
            binding.textvieworderreadac7.append("\nTotal Cost $totalcost")

            binding.apply {
                recyclerviewresadorders.layoutManager=LinearLayoutManager(this@MainActivity7)
                favoriteAdapter=FavoriteAdapter(this@MainActivity7,it)
                recyclerviewresadorders.adapter=favoriteAdapter
            }
        })

        binding.buttonbuy.setOnClickListener {
            val calendar=Calendar.getInstance()
            var day=calendar.get(Calendar.DAY_OF_MONTH)
            var month=calendar.get(Calendar.MONTH)+1
            var year=calendar.get(Calendar.YEAR)
            var datatime="$day/$month/$year"
            productViewModel.buy(
                orders =binding.textvieworderreadac7.text.toString(),
                username = username,
                surname = surname!!,
                phone = phone!!,
                adress = adress!!,
                datatime = datatime
            )
        }

        productViewModel.buysucces().observe(this@MainActivity7,{
            if (it){
                Toast.makeText(this@MainActivity7,"Zakaz berildi",Toast.LENGTH_SHORT).show()
            }
        })

    }
}