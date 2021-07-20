package com.gtappdevelopers.noteapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), NoteClickInterface {

    //on below line we are creating a variable for our recycler view, exit text, button and viewmodal.
    lateinit var viewModal: NoteViewModal
    lateinit var notesRV: RecyclerView
    lateinit var noteEdt: EditText
    lateinit var addNoteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //on below line we are initializing all our variables.
        notesRV = findViewById(R.id.notesRV)
        noteEdt = findViewById(R.id.idEdtNote)
        addNoteBtn = findViewById(R.id.idBtnAddNote)
        //on below line we are setting layout manager to our recycler view.
        notesRV.layoutManager = LinearLayoutManager(this)
        //on below line we are initializing our adapter class.
        val noteRVAdapter = NoteRVAdapter(this, this)
        //on below line we are setting adapter to our recycler view.
        notesRV.adapter = noteRVAdapter
        //on below line we are initializing our view modal.
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)
        //on below line we are calling all notes methof from our view modal class to observer the changes on list.
        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                //on below line we are updating our list.
                noteRVAdapter.updateList(it)
            }
        })
        //on below line we are adding a click listner to our add notes button.

        addNoteBtn.setOnClickListener {
            //on below line we are getting the data from our edit text.
            val noteText = noteEdt.text.toString()
            //on below line we are checking weather the string is empty or not.
            if (noteText.isNotEmpty()) {
                //if the string is not empty we are calling a add note method to add data to our room database.
                viewModal.addNote(Note(noteText))
                Toast.makeText(this, "$noteText Added", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNoteClick(note: Note) {
        //in on note click method we are calling delete method from our viw modal to delete our not.
        viewModal.deleteNote(note)
        //displaying a toast message
        Toast.makeText(this, "${note.text} Deleted", Toast.LENGTH_LONG).show()
    }
}