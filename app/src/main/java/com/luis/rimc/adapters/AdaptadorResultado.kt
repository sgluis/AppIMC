package com.luis.rimc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luis.rimc.R
import com.luis.rimc.model.Resultado

class AdaptadorResultado(var listaResultadp: ArrayList<Resultado>, var context: Context) :
    RecyclerView.Adapter<AdaptadorResultado.MyHoler>() {
    class MyHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen = itemView.findViewById<ImageView>(R.id.imagen)
        val texto = itemView.findViewById<TextView>(R.id.texto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoler {
        val vista = LayoutInflater.from(context).inflate(R.layout.item_resultado, parent, false)
        val holder = MyHoler(vista)
        return holder
    }

    override fun getItemCount(): Int {
        return listaResultadp.size
    }

    override fun onBindViewHolder(holder: MyHoler, position: Int) {
        val resultado: Resultado = listaResultadp[position]
        holder.imagen.setImageResource(resultado.icono)
        holder.texto.text = resultado.nombre
    }

    fun actualizarLista(nuevaLista: ArrayList<Resultado>) {
        this.listaResultadp = nuevaLista
        notifyDataSetChanged()
    }
}
