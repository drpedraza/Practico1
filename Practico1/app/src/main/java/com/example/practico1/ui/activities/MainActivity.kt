package com.example.practico1.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practico1.R
import com.example.practico1.models.Notes
import com.example.practico1.ui.adapters.NotesAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NotesAdapter.OnNotesClickListener {
    private val datalist = arrayListOf(
        Notes("Bienvenido"),
    )
    private lateinit var rvNotas: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fabAdd = findViewById(R.id.fabAdd)
        rvNotas = findViewById(R.id.rvNotas)
        setupRecyclerView()
        setupEventListeners()
    }

    private fun setupEventListeners() {
        fabAdd.setOnClickListener {
            buildAlertDialog()
        }
    }

    private fun setupRecyclerView() {
        rvNotas.adapter = NotesAdapter(datalist, this)
        rvNotas.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
    }

    private fun buildAlertDialog(notes: Notes? = null) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(if (notes != null) "Editar Nota" else "Agregar Nota")


        val viewInflated: View = LayoutInflater.from(this)
            .inflate(R.layout.agregar_activity, null, false)

        val txtNewNote: EditText = viewInflated.findViewById(R.id.txtNewNote)
        txtNewNote.setText(notes?.description)
        builder.setView(viewInflated)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            val description = txtNewNote.text.toString()
            if (notes != null) {
                notes.description = description
                editNoteFromList(notes)
            } else {
                addNotaToList(description)
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun addNotaToList(description: String) {
        val note = Notes(description)
        val adapter = rvNotas.adapter as NotesAdapter
        adapter.itemAdded(note)
    }

    private fun editNoteFromList(note: Notes) {
        val adapter = rvNotas.adapter as NotesAdapter
        adapter.itemUpdated(note)
    }


    override fun onNoteEdit(note: Notes) {
        buildAlertDialog(note)
    }

    override fun onNoteDelete(note: Notes) {
        val adapter = rvNotas.adapter as NotesAdapter
        adapter.itemDeleted(note)
    }

}