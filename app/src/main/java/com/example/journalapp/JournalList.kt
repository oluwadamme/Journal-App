package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journalapp.databinding.ActivityJournalListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class JournalList : AppCompatActivity() {
    private lateinit var binding: ActivityJournalListBinding
    private val collectionName="Journals"
    // firebase instances
    private lateinit var user:FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth
   private val db= FirebaseFirestore.getInstance()
    val collectionReference=db.collection(collectionName)
    private lateinit var storage:StorageReference

    private lateinit var journalList: MutableList<Journal>
    private lateinit var adapter: JournalRecyclerAdapter

    private lateinit var noPostTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_journal_list)

        //firebase auth
        firebaseAuth=Firebase.auth
        user=firebaseAuth.currentUser!!

        // recyclerView
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        //
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.journal_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add->{
                val intent=Intent(this,AddJournalActivity::class.java)
               startActivity(intent)
            }
            R.id.action_sign_out->{

                firebaseAuth.signOut()
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        collectionReference.whereEqualTo("userId",JournalUser.instance?.userId)
            .get().addOnSuccessListener { it ->
                if (!it.isEmpty){
                    it.forEach {
                      val journal:Journal=  it.toObject<Journal>()
                        journalList.add(journal)
                    }
                    adapter=JournalRecyclerAdapter(journalList)
                    binding.recyclerView.adapter=adapter
                    adapter.notifyDataSetChanged()
                }else{
                    binding.emptyListText.visibility=View.VISIBLE
                }

            }.addOnFailureListener {
                Toast.makeText(this,"Opps, something well wrong",Toast.LENGTH_SHORT).show()
            }
    }
}