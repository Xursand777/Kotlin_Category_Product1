package com.x7.kotlin_category_product.model.repositories

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.x7.kotlin_category_product.model.CategoryModel
import com.x7.kotlin_category_product.model.ImageModel
import com.x7.kotlin_category_product.model.OrderModel
import com.x7.kotlin_category_product.model.ProductModel
import com.x7.kotlin_category_product.utilits.Constants.ADMINORDER
import com.x7.kotlin_category_product.utilits.Constants.ALLPRODUCTS
import com.x7.kotlin_category_product.utilits.Constants.HISTORY
import com.x7.kotlin_category_product.utilits.Constants.IMAGES
import com.x7.kotlin_category_product.utilits.Constants.ORDERS

import com.x7.kotlin_category_product.utilits.Constants.PRODUCTS
import com.x7.kotlin_category_product.utilits.Constants.USERNAME

class RepositoryProduct constructor(
    var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference().child(PRODUCTS),
    var databaseReferenceimages: DatabaseReference = FirebaseDatabase.getInstance().getReference().child(IMAGES),
    var databaseReferenceall: DatabaseReference = FirebaseDatabase.getInstance().getReference().child(ALLPRODUCTS),
    var storageReference: StorageReference = FirebaseStorage.getInstance().getReference().child(PRODUCTS),
    var databaseReferenceorder: DatabaseReference=FirebaseDatabase.getInstance().getReference().child(ORDERS),
    var databaseReferenceadminorder: DatabaseReference=FirebaseDatabase.getInstance().getReference().child(ADMINORDER),
    var databaseReferencehistory: DatabaseReference=FirebaseDatabase.getInstance().getReference().child(HISTORY),

) {
    var livedatasucces=MutableLiveData<Boolean>()
    var livedataprogress=MutableLiveData<Double>()

     val livedataarrraylisttwo = MutableLiveData<ArrayList<ProductModel>>()
    val arrayList = ArrayList<ProductModel>()

    //productallimages
    var livedataproductallimages=MutableLiveData<ArrayList<ImageModel>>()
    var arrayListproductallimages=ArrayList<ImageModel>()

    //ORDER
    var liveDataallorder=MutableLiveData<ArrayList<ProductModel>>()
    var arrayListallorder=ArrayList<ProductModel>()
    //Buy
    var livedatabuy=MutableLiveData<Boolean>()
    fun addproduct(
        categoryname:String,
        name: String,
        uri: Uri,
        price: String,
        description: String,
        arraylistimage: ArrayList<Uri>
    ){

        //realtime added
        var pushkey = databaseReference.push().key.toString()
        //Realtime Database
        val product = ProductModel(
            name = name,
            imguri = uri.toString(),
            price = price,
            description = description,
            pushkey = pushkey
        )

        databaseReference.child(categoryname).child(pushkey).setValue(product)
        if (uri != null) {
             succes(true)
            val filereference: StorageReference = storageReference.child(System.currentTimeMillis().toString() + "." + System.currentTimeMillis().toString())
            filereference.putFile(uri)
                .addOnSuccessListener {
                    filereference.downloadUrl.addOnSuccessListener {rasm->
                        val product=ProductModel(name,rasm.toString(),price,description,pushkey)
                        databaseReference.child(categoryname).child(pushkey).setValue(product)
                        databaseReferenceall.child(pushkey).setValue(product).addOnCompleteListener {
                            if (it.isSuccessful){
                                succes(false)
                            }
                        }


                    }
                }
                .addOnProgressListener {
                    val progress: Double = 100.0 * it.getBytesTransferred() / it.getTotalByteCount()
                      livedataprogress.value=progress
                }
        }


        //realtime added
        //add images
        for (imguri in arraylistimage){
            if (imguri != null){
                succes(true)
                val filereference: StorageReference = storageReference.child(System.currentTimeMillis().toString() + "." + System.currentTimeMillis().toString())
                filereference.putFile(imguri)
                    .addOnSuccessListener {
                        filereference.downloadUrl.addOnSuccessListener {rasm->
                            val imageModel = ImageModel(imageurl = rasm.toString())
                            databaseReferenceimages.child(pushkey).push().setValue(imageModel)
                            succes(false)


                        }
                    }
                    .addOnProgressListener {
                        val progress: Double = 100.0 * it.getBytesTransferred() / it.getTotalByteCount()
                        livedataprogress.value=progress
                    }
            }
        }


    }

    fun productallimages(pushkey:String):MutableLiveData<ArrayList<ImageModel>>{
        databaseReferenceimages.child(pushkey).addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                arrayListproductallimages.clear()

                for (datasnapshot:DataSnapshot in snapshot.children){
                    val imageModel=datasnapshot.getValue(ImageModel::class.java)
                    arrayListproductallimages.add(imageModel!!)
                }
                livedataproductallimages.value=arrayListproductallimages


            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        return livedataproductallimages

    }

    //shu yerda yangi fun ochib allproducts dagilarni o`qib olish code sini yozaman
    fun readfromsfirebasetwo():MutableLiveData<ArrayList<ProductModel>>{
          databaseReferenceall.addValueEventListener(object :ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                  arrayList.clear()

                  for (datasnapshop:DataSnapshot in snapshot.children){
                      val productM=datasnapshop.getValue(ProductModel::class.java)
                      arrayList.add(productM!!)
                  }
                  livedataarrraylisttwo.value=arrayList

              }

              override fun onCancelled(error: DatabaseError) {

              }
          })
        return livedataarrraylisttwo
    }

    fun readeverycategory(categoryname: String):MutableLiveData<ArrayList<ProductModel>>{
        databaseReference.child(categoryname).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()

                for (datasnapshop:DataSnapshot in snapshot.children){
                    val productM=datasnapshop.getValue(ProductModel::class.java)
                    arrayList.add(productM!!)
                }
                livedataarrraylisttwo.value=arrayList

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return livedataarrraylisttwo
    }


    fun succes(boolean: Boolean){
       livedatasucces.value=boolean
    }

    //Order
    @SuppressLint("SuspiciousIndentation")
    fun neworder(
        name: String,
        imguri:String,
        price: String,
        description: String,
        pushkey: String,
    ){
      val productModel=ProductModel(
          name = name,
          imguri = imguri,
          price = price,
          description = description,
          pushkey = pushkey
      )

        databaseReferenceorder.child(USERNAME).child(pushkey).setValue(productModel)
    }

    //READ ORDER
    fun readallorders(username:String):MutableLiveData<ArrayList<ProductModel>>{
        databaseReferenceorder.child(username).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayListallorder.clear()
                for (datasnapshot:DataSnapshot in snapshot.children){
                    var productModel=datasnapshot.getValue(ProductModel::class.java)
                    arrayListallorder.add(productModel!!)
                }
                liveDataallorder.value=arrayListallorder
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return liveDataallorder
    }

    //Buy
    fun buyproduct(
        orders :String,
        username :String,
        surname :String,
        phone :String,
        adress :String,
        datatime :String
    ){
        livedatabuy.value=false

        val orderModel=OrderModel(
            orders = orders,
            username = username,
            surname = surname,
            phone = phone,
            adress = adress,
            datatime = datatime
        )
        databaseReferenceadminorder.push().setValue(orderModel)
        databaseReferencehistory.child(phone).push().setValue(orderModel).addOnCompleteListener {
            if (it.isSuccessful){
                livedatabuy.value=true
                databaseReferenceorder.child(username).removeValue()
            }
        }

    }
    //Buy


}