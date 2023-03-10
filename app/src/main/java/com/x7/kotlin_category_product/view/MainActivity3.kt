package com.x7.kotlin_category_product.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.x7.kotlin_category_product.databinding.ActivityMain3Binding
import com.x7.kotlin_category_product.view.adapters.SelectAdapter
import com.x7.kotlin_category_product.viewmodel.ProductViewModel

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    var categoryname:String?=null
    var categoryimage:String?=null
    var imageuri:Uri?=null
    lateinit var productViewModel: ProductViewModel
    var arraylistimage = ArrayList<Uri>()
    lateinit var selectAdapter:SelectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        categoryname=intent.getStringExtra("name")
        categoryimage=intent.getStringExtra("image")
        supportActionBar!!.hide()
        productViewModel=ViewModelProvider(this@MainActivity3).get(ProductViewModel::class.java)

        binding.apply {
            categorynametitle.text=categoryname
            Glide.with(this@MainActivity3).load(categoryimage).into(circleimageviewcategory)

            imageviewopengallery2.setOnClickListener {
             openFileChooser()
            }

            buttonaddproduct.setOnClickListener{
                 productViewModel.addnewproduct(
                     categoryname!!,
                     edittexproductname.text.toString(),
                     imageuri!!,
                     edittexprice.text.toString(),
                     edittexDescription.text.toString(),
                     arraylistimage
                 )
            }
            //Horizontalprogressbar
            productViewModel.uploadsucces().observe(this@MainActivity3,{
                if (it){
                    showprogress()
                }else{
                    hideprogress()
                   // finish()
                  //  startActivity(Intent(this@MainActivity3,MainActivity::class.java))
                }
            })
               //progress foiz %
            productViewModel.uploadproductprogress().observe(this@MainActivity3,{
                textviewprogressac3.text="${it.toInt()} %"
                progressBarhorizontalac3.progress=it.toInt()
            })


        }



    }
    //Open Gallery
    fun openFileChooser() {
        getContent.launch("image/*")
    }
    //Open Gallery and Set image to imageview
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        binding.imageviewopengallery2.setImageURI(uri)
        if (uri != null) {
            imageuri = uri
            arraylistimage.add(uri)
            binding.buttonaddproduct.isEnabled=true
            binding.recyclerviewselectimages.layoutManager=LinearLayoutManager(this@MainActivity3,RecyclerView.HORIZONTAL,false)
            selectAdapter=SelectAdapter(this@MainActivity3,arraylistimage)
            binding.recyclerviewselectimages.adapter=selectAdapter

        }
    }

    //show progress hide progress
     fun showprogress(){
         binding.apply {
            progressBarhorizontalac3.visibility=View.VISIBLE
            textviewprogressac3.visibility=View.VISIBLE
         }
     }
    fun hideprogress(){
        binding.apply {
            progressBarhorizontalac3.visibility=View.GONE
            textviewprogressac3.visibility=View.GONE
        }
    }
}