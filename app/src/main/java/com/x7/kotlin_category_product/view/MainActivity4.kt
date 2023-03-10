package com.x7.kotlin_category_product.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView.ScaleType
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.*
import com.x7.kotlin_category_product.R
import com.x7.kotlin_category_product.databinding.ActivityMain4Binding
import com.x7.kotlin_category_product.model.ImageModel
import com.x7.kotlin_category_product.model.ProductModel
import com.x7.kotlin_category_product.utilits.Constants.IMAGES
import com.x7.kotlin_category_product.viewmodel.ProductViewModel

class MainActivity4 : AppCompatActivity() {
    lateinit var binding: ActivityMain4Binding
    lateinit var databaseReference: DatabaseReference
    var arrayList=ArrayList<ImageModel>()
    val imagelist= ArrayList<SlideModel>()
    lateinit var productViewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
         var product:ProductModel=intent.getSerializableExtra("product") as ProductModel
           // Glide.with(this@MainActivity4).load(product!!.imageurl).into(imageviewforview)
            var pushkey=product.pushkey
            databaseReference=FirebaseDatabase.getInstance().getReference().child(IMAGES).child(pushkey!!)
            textviewproductname.text="${product!!.name}"
            textviewproductdesriptionandprice.text="${product!!.price}\n${product!!.description}"

            databaseReference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrayList.clear()
                    for (datasnapshot:DataSnapshot in snapshot.children){
                        val imageModel=datasnapshot.getValue(ImageModel::class.java)
                        arrayList.add(imageModel!!)
                    }
                    for (img in arrayList){
                        imagelist.add(SlideModel(img.imageurl,ScaleTypes.CENTER_CROP))
                    }
                    imageslider.setImageList(imagelist)

                    Toast.makeText(this@MainActivity4,"${arrayList.size}",Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


            //ORDER
            productViewModel=ViewModelProvider(this@MainActivity4).get(ProductViewModel::class.java)
            buttonaddtobadge.setOnClickListener {
                productViewModel.addneworder(
                    name = product.name!!,
                    imguri = product.imguri!!,
                    price = product.price!!,
                    description = product.description!!,
                    pushkey = product.pushkey!!
                )

            }
        }


    }
}