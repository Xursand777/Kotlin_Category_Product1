package com.x7.kotlin_category_product.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.x7.kotlin_category_product.R
import com.x7.kotlin_category_product.databinding.ActivityMain5Binding
import com.x7.kotlin_category_product.model.User
import com.x7.kotlin_category_product.utilits.Constants.USER_INFORMATION

class MainActivity5 : AppCompatActivity() {
    lateinit var binding: ActivityMain5Binding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference().child(USER_INFORMATION)
        binding.apply {

            buttonreg.setOnClickListener {
                firebaseAuth.createUserWithEmailAndPassword(edittextreglogin.text.toString(),edittextregpassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        var uid=firebaseAuth.currentUser!!.uid
                        val user=User(
                            name = edittextregname.text.toString(),
                            surname = edittextregsurname.text.toString(),
                            phone = edittextregnumber.text.toString(),
                            adress = edittextregadress.text.toString(),
                            login = edittextreglogin.text.toString(),
                            password = edittextregpassword.text.toString(),
                            uid = uid
                        )
                        databaseReference.child(uid).setValue(user)
                        Toast.makeText(this@MainActivity5,"Registered Succesfully",Toast.LENGTH_LONG).show()

                    }
                }
            }

        }


    }
}