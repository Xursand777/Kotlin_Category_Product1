package com.x7.kotlin_category_product.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.x7.kotlin_category_product.R
import com.x7.kotlin_category_product.databinding.ActivityMain6Binding

class MainActivity6 : AppCompatActivity() {
    lateinit var binding: ActivityMain6Binding
    lateinit var firebaseAuth: FirebaseAuth
    var uid:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain6Binding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()
        load()

        binding.apply {
            textviewregopen.setOnClickListener {
                startActivity(Intent(this@MainActivity6,MainActivity5::class.java))
            }

            buttonlog.setOnClickListener {
                progressbarlogin.visibility=View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(edittextloglogin.text.toString(),edittextlogpassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        uid=firebaseAuth.currentUser!!.uid
                        val intent=Intent(this@MainActivity6,MainActivity::class.java)
                        intent.putExtra("uid",uid)
                        startActivity(intent)
                        progressbarlogin.visibility=View.GONE
                        save(uid)
                    }else{
                        progressbarlogin.visibility=View.GONE
                        Toast.makeText(this@MainActivity6,"Error",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }




    fun save(text: String?) {
        val editor = getSharedPreferences("Pr", MODE_PRIVATE).edit() as SharedPreferences.Editor
        editor.putString("pr", text)
        editor.commit()
    }

    fun load() {
        val sharedPreferences = getSharedPreferences("Pr", MODE_PRIVATE)
        val savedtext = sharedPreferences.getString("pr", null)
        if (savedtext!=null){
            val intent=Intent(this@MainActivity6,MainActivity::class.java)
            intent.putExtra("uid",savedtext)
            startActivity(intent)
        }


    }
}