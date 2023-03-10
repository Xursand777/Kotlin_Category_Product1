package com.x7.kotlin_category_product.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.x7.kotlin_category_product.databinding.ActivityMainBinding
import com.x7.kotlin_category_product.model.CategoryModel
import com.x7.kotlin_category_product.model.ProductModel
import com.x7.kotlin_category_product.utilits.Constants
import com.x7.kotlin_category_product.utilits.Constants.IMAGES
import com.x7.kotlin_category_product.utilits.Constants.USERNAME
import com.x7.kotlin_category_product.utilits.Constants.USER_INFORMATION
import com.x7.kotlin_category_product.view.adapters.CategoryAdapter
import com.x7.kotlin_category_product.view.adapters.ProductAdapter
import com.x7.kotlin_category_product.viewmodel.CategoryViewModel
import com.x7.kotlin_category_product.viewmodel.ProductViewModel
import com.x7.kotlin_category_product.R


class MainActivity : AppCompatActivity() {
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var productAdapter: ProductAdapter
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var productViewModel: ProductViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var databaseReferenceproductimages: DatabaseReference
    var databaseReferenceorder: DatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.ORDERS)
    var arrayListallorder = ArrayList<ProductModel>()
    var useruid: String? = null
    var admin = false

    //Search
    var productarraylist=ArrayList<ProductModel>()
    var categoryarraylist=ArrayList<CategoryModel>()
    //

    var surname: String = ""
    var phone: String = ""
    var adress: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        categoryViewModel = ViewModelProvider(this@MainActivity).get(CategoryViewModel::class.java)
        productViewModel = ViewModelProvider(this@MainActivity).get(ProductViewModel::class.java)
        useruid = intent.getStringExtra("uid")
        if (useruid == "eIW3docxyEgkze4o95KsmIXQSun2") {
            binding.badgecounter.visibility = View.INVISIBLE
            admin = true
            binding.imageviewopenac2.visibility = View.VISIBLE
        }
        databaseReference =
            FirebaseDatabase.getInstance().getReference().child(USER_INFORMATION).child(useruid!!)
        databaseReferenceproductimages = FirebaseDatabase.getInstance().getReference().child(IMAGES)

        val view = binding.navigationview1.getHeaderView(0)
        val textViewusername: TextView =
            view.findViewById(R.id.textviewheaderusername)
        val textViewemail: TextView =
            view.findViewById(R.id.textviewheaderemail)
        val textViewphone: TextView =
            view.findViewById(R.id.textviewheaderphone)



        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var name = snapshot.child("name").getValue().toString()
                var surnamee = snapshot.child("surname").getValue().toString()
                var phonee = snapshot.child("phone").getValue().toString()
                var address = snapshot.child("adress").getValue().toString()
                var login = snapshot.child("login").getValue().toString()
                var password = snapshot.child("password").getValue().toString()
                USERNAME = name
                surname = surnamee
                phone = phonee
                adress = address

                binding.textviewtitle.text = name
                textViewusername.text = "$name $surname"
                textViewemail.text = login
                textViewphone.text = phone





                databaseReferenceorder.child(name)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            arrayListallorder.clear()
                            for (datasnapshot: DataSnapshot in snapshot.children) {
                                var productModel = datasnapshot.getValue(ProductModel::class.java)
                                arrayListallorder.add(productModel!!)
                            }
                            binding.badgecounter.text = "${arrayListallorder.size}"
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        binding.apply {
            imageviewopenac2.setOnClickListener {
                startActivity(Intent(this@MainActivity, MainActivity2::class.java))
            }

            edittextsearch.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    filter(p0.toString())
                }

            })

            imagevieworder.setOnClickListener {
                if (useruid == "eIW3docxyEgkze4o95KsmIXQSun2") {

                    val intent = Intent(this@MainActivity, MainActivity9::class.java)
                    startActivity(intent)
                } else {
                    badgecounter.visibility = View.VISIBLE
                    val intent = Intent(this@MainActivity, MainActivity7::class.java)
                    intent.putExtra("username", USERNAME)
                    intent.putExtra("surname", surname)
                    intent.putExtra("phone", phone)
                    intent.putExtra("adress", adress)
                    startActivity(intent)
                }


            }


            recyclerview1.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            categoryViewModel.readalldatafirebase().observe(this@MainActivity, {
                categoryarraylist=it
                categoryAdapter = CategoryAdapter(this@MainActivity, it)
                recyclerview1.adapter = categoryAdapter
            })
            recyclerview2.layoutManager = GridLayoutManager(this@MainActivity, 3)
            productViewModel.readalldatafirebasetwo().observe(this@MainActivity, {
                it.shuffle()
                productAdapter = ProductAdapter(this@MainActivity, it)
                recyclerview2.adapter = productAdapter
                productarraylist=it
            })

            imageviewopennavigation.setOnClickListener {
                drawerlayout.openDrawer(Gravity.LEFT)
            }

            navigationview1.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.item1 -> {
                        val intent = Intent(this@MainActivity, MainActivity8::class.java)
                        intent.putExtra("phone", phone)
                        startActivity(intent)
                    }
                    R.id.item2 -> {
                        save(null)
                        startActivity(Intent(this@MainActivity, MainActivity6::class.java))
                    }
                }

                return@setNavigationItemSelectedListener true
            }
            //ORDER BADGE


        }

    }

    fun categorychanged(categoryname: String) {
        productViewModel.readeverycategory(categoryname).observe(this@MainActivity, {
            productAdapter = ProductAdapter(this@MainActivity, it)
            binding.recyclerview2.adapter = productAdapter
        })
    }


    fun save(text: String?) {
        val editor = getSharedPreferences("Pr", MODE_PRIVATE).edit() as SharedPreferences.Editor
        editor.putString("pr", text)
        editor.commit()
    }


    //Search Filter

    fun filter(text:String){

           //Products
        val searcharraylist2=ArrayList<ProductModel>()
        for (item:ProductModel in productarraylist){
            if (item.name?.toLowerCase()!!.contains(text.toLowerCase())){
                searcharraylist2.add(item)
            }
        }
        productAdapter?.let {
            it.filterList(searcharraylist2)
        }
        //Categories
        val searcharraylist3=ArrayList<CategoryModel>()
        for (item:CategoryModel in categoryarraylist){
            if (item.name?.toLowerCase()!!.contains(text.toLowerCase())){
                searcharraylist3.add(item)
            }
        }
        categoryAdapter?.let {
            it.filterList(searcharraylist3)
        }

        }
}