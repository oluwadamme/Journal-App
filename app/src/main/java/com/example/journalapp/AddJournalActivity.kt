package com.example.journalapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivityAddJournalBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Date

class AddJournalActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddJournalBinding

    var currentUserId=""
    var currentUserName=""

    var imageUrl:Uri?=null

    // firebase instances
    private lateinit var user: FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth

    // firebase firestore
    private val collectionName="Journals"
    private val db= FirebaseFirestore.getInstance()
    private val collectionReference=db.collection(collectionName)
    private lateinit var storage: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil. setContentView(this,R.layout.activity_add_journal)
        firebaseAuth= Firebase.auth
        storage = FirebaseStorage.getInstance().getReference()
        binding.apply {
            postProgress.visibility=View.INVISIBLE
            if (JournalUser.instance!=null){
                currentUserId=firebaseAuth.currentUser?.uid.toString()
                currentUserName=firebaseAuth.currentUser?.displayName.toString()
                postUsername.text=currentUserName
            }
            postCamera.setOnClickListener{
                var intent=Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                startActivityForResult(intent,1)
            }
            postSaveBtn.setOnClickListener(){
                savePost()
            }
        }

    }

    private fun savePost() {
        val title:String= binding.postTitle.text.toString().trim()
        val desc:String= binding.postDesc.text.toString().trim()
        binding.postProgress.visibility=View.VISIBLE
        if (!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(desc)&&imageUrl!=null){
            // saving images in storage : .../journal_images/my_image.png

            val filePath:StorageReference=storage.child("journal_images")
                .child("my_image"+Timestamp.now().seconds)

            // uploading image
            filePath.putFile(imageUrl!!).addOnSuccessListener {
                filePath.downloadUrl.addOnSuccessListener {
                    val imageUrl=it.toString()
                    val timestamp:Timestamp= Timestamp(Date())

                    val journal=Journal(title,desc,imageUrl,timestamp,currentUserId,currentUserName)
                    collectionReference.add(journal).addOnSuccessListener {
                        binding.postProgress.visibility=View.INVISIBLE
                        val intent=Intent(this,JournalList::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }.addOnFailureListener{
                binding.postProgress.visibility=View.INVISIBLE
            }

        }else{
            binding.postProgress.visibility=View.INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1&&resultCode== RESULT_OK){
            if (data!=null){
                imageUrl=data.data
                binding.postImageView.setImageURI(imageUrl)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        user= firebaseAuth.currentUser!!

    }
}