package com.example.scannerqr.misc

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.scannerqr.R

class PengumumanAdapter(private val isiliast : List<Pengumuman>) : Adapter<PengumumanAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val isi: TextView
        val tanggal: TextView

        init {
            // Define click listener for the ViewHolder's View.
            isi = view.findViewById(R.id.isi)
            tanggal = view.findViewById(R.id.tanggal)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_pengumuman, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.isi.text = isiliast[position].isi_pengumuman
        viewHolder.tanggal.text = isiliast[position].tgl_pengumuman
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = isiliast.size
}