package com.example.scannerqr.misc

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.scannerqr.R

class AdapterRiwayat(private val isiliast : List<Riwayat>) : Adapter<AdapterRiwayat.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tanggalRiwayat: TextView
        val waktuRiwayat: TextView
        val keteranganRiwayat: TextView

        init {
            // Define click listener for the ViewHolder's View.
            tanggalRiwayat = view.findViewById(R.id.tanggalriwayat)
            waktuRiwayat = view.findViewById(R.id.wakturiwayat)
            keteranganRiwayat = view.findViewById(R.id.keteranganriwayat)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_riwayat, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tanggalRiwayat.text = isiliast[position].tanggal_presensi
        viewHolder.waktuRiwayat.text = isiliast[position].waktu_presensi
        viewHolder.keteranganRiwayat.text = isiliast[position].keterangan
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = isiliast.size
}