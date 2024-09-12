package com.example.practico1.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.practico1.R
import com.example.practico1.models.Notes

class NotesAdapter(
    private val notesList: ArrayList<Notes>,
    private val listener: OnNotesClickListener
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val colors = listOf(
        "#FFCDD2", "#F8BBD0", "#E1BEE7", "#D1C4E9", "#C5CAE9",
        "#BBDEFB", "#B3E5FC", "#B2EBF2", "#B2DFDB", "#FFFFFF    "
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_activity, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notesList[position], listener, colors)
    }

    fun itemAdded(note: Notes) {
        notesList.add(0, note)
        notifyItemInserted(0)
    }

    fun itemDeleted(note: Notes) {
        val index = notesList.indexOf(note)
        notesList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun itemUpdated(note: Notes) {
        val index = notesList.indexOf(note)
        notesList[index] = note
        notifyItemChanged(index)
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var txtItemNote = itemView.findViewById<TextView>(R.id.txtItemNote)
        private var btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
        private var btnEdit = itemView.findViewById<ImageButton>(R.id.btnEdit)
        private var btnColor = itemView.findViewById<ImageButton>(R.id.btnColor)

        fun bind(note: Notes, listener: OnNotesClickListener, colors: List<String>) {
            txtItemNote.text = note.description
            itemView.setBackgroundColor(android.graphics.Color.parseColor(note.color))

            btnEdit.setOnClickListener {
                listener.onNoteEdit(note)
            }

            btnDelete.setOnClickListener {
                listener.onNoteDelete(note)
            }

            btnColor.setOnClickListener {
                showColor(note, colors)
            }
        }

        private fun showColor(note: Notes, colors: List<String>) {
            val builder = AlertDialog.Builder(itemView.context)
            val inflater = LayoutInflater.from(itemView.context)
            val view = inflater.inflate(R.layout.color_palette_activity, null)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            val colorPalette = view.findViewById<GridLayout>(R.id.colorPalette)

            for (i in 0 until colorPalette.childCount) {
                val colorView = colorPalette.getChildAt(i)
                colorView.setOnClickListener {
                    val selectedColor = colors[i]
                    itemView.setBackgroundColor(android.graphics.Color.parseColor(selectedColor))
                    note.color = selectedColor
                    dialog.dismiss()
                }
            }
        }
    }

    public interface OnNotesClickListener {
        fun onNoteEdit(note: Notes)
        fun onNoteDelete(note: Notes)
    }
}




