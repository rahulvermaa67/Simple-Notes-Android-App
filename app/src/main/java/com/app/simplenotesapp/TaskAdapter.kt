package com.app.simplenotesapp


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class TaskAdapter(context: Context, private val tasks: MutableList<String>, private val onDeleteClick: (Int) -> Unit) : ArrayAdapter<String>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val textViewTask: TextView = view.findViewById(R.id.textViewTask)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)

        textViewTask.text = task
        buttonDelete.setOnClickListener {
            onDeleteClick(position)
        }

        return view
    }
}
