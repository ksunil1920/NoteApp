package com.sky.notes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sky.notes.databinding.NoteItemBinding
import com.sky.notes.model.NoteResponse

class NoteAdapter(private val onNoteClicked: (NoteResponse) -> Unit): ListAdapter<NoteResponse, NoteAdapter.NoteViewHolder>(
    CompareDiffUtil()) {


    inner class NoteViewHolder(private val binding: NoteItemBinding) :RecyclerView.ViewHolder(binding.root){

       fun bind(noteResponse: NoteResponse ){
           binding.title.text = noteResponse.title
           binding.desc.text = noteResponse.description
           binding.root.setOnClickListener{
               onNoteClicked(noteResponse)
           }
       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    class CompareDiffUtil:DiffUtil.ItemCallback<NoteResponse>(){
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }

    }
}