package com.example.scannerqr.misc

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.scannerqr.R

class AdapterMapel(private val isiliast : List<Mapel>) : Adapter<AdapterMapel.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaMapel: TextView

        init {
            // Define click listener for the ViewHolder's View.
            namaMapel = view.findViewById(R.id.mapelnama)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_mapel, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.namaMapel.text = isiliast[position].namaMapel
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = isiliast.size
}