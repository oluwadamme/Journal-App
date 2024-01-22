package com.example.journalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.databinding.JournalItemBinding
import com.squareup.picasso.Picasso

class JournalRecyclerAdapter(private val journalList: List<Journal>) :RecyclerView.Adapter<JournalRecyclerAdapter.MyViewHolder>(){
    class MyViewHolder(val binding: JournalItemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding:JournalItemBinding = DataBindingUtil.inflate(inflater,R.layout.journal_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return journalList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val journal=journalList[position]
        Picasso.get().load(journal.image).into(holder.binding.imageView);
        holder.binding.journal=journal
//
//        holder.binding.imageView.setImageResource(journal.image)
//        holder.binding.descText.text=journal.desc
//        holder.binding.titleText.text=journal.title
//        holder.binding.timeStamp.text=journal.timeAdded.time.toString()
//        holder.binding.shareButton.setOnClickListener(){
//
//        }
    }
}