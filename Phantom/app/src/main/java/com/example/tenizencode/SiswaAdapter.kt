package com.example.tenizencode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SiswaAdapter(
    private val siswaList: MutableList<Siswa>,
    private val listener: OnAdapterListener
) : RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder>() {

    interface OnAdapterListener {
        fun onEditClick(siswa: Siswa)
        fun onDeleteClick(siswa: Siswa, position: Int)
    }

    class SiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoto: ImageView = itemView.findViewById(R.id.ivFotoSiswa)
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaSiswa)
        val tvNis: TextView = itemView.findViewById(R.id.tvNis)
        val tvGender: TextView = itemView.findViewById(R.id.tvGender)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvTanggalLahir: TextView = itemView.findViewById(R.id.tvTanggalLahir)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return SiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        val siswa = siswaList[position]

        holder.tvNama.text = siswa.namasiswa
        holder.tvNis.text = "NIS: ${siswa.nis}"
        holder.tvGender.text = "Gender: ${siswa.gender}"
        holder.tvAlamat.text = siswa.alamat
        holder.tvTanggalLahir.text = "Lahir: ${siswa.tanggallahir}"

        Glide.with(holder.itemView.context)
            .load(siswa.foto)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.ivFoto)

        holder.btnEdit.setOnClickListener {
            listener.onEditClick(siswa)
        }

        holder.btnDelete.setOnClickListener {
            listener.onDeleteClick(siswa, position)
        }
    }

    override fun getItemCount(): Int = siswaList.size

    fun removeItem(position: Int) {
        if (position >= 0 && position < siswaList.size) {
            siswaList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
