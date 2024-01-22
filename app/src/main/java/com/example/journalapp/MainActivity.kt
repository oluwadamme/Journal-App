package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        auth= Firebase.auth
        binding.createAccountBtn.setOnClickListener(){
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener(){
            val email= binding.loginEmail.text.toString().trim()
            val pass= binding.loginPassword.text.toString().trim()
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this) {
                if (it.isSuccessful){
                    val journal:JournalUser=JournalUser.instance!!
                    journal.userId=auth.currentUser?.uid
                    journal.username=auth.currentUser?.displayName
                    val intent=Intent(this, JournalList::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
                }

            }
        }


    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser!=null){
            Log.i("Tagy",""+auth.currentUser?.uid)
            val intent=Intent(this, JournalList::class.java)
            startActivity(intent)
        }
    }
}