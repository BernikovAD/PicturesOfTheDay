package com.example.picturesoftheday.view.favoritePOD

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesoftheday.databinding.ItemsBinding
import com.example.picturesoftheday.repository.DataPOD
import com.example.picturesoftheday.view.settings.ItemTouchHelperViewHolder

class AdapterRecyclerView : RecyclerView.Adapter<AdapterRecyclerView.MyViewHolder>() {

    private var data: List<DataPOD> = arrayListOf()


    fun setData(data: List<DataPOD>) {
        this.data = data
        notifyDataSetChanged()
    }
    private lateinit var listener: OnClickAdapterItem;

    public fun setListener(listener: OnClickAdapterItem){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemsBinding =
            ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

/*    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Weather) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.textViewHistory.text =
                    String.format("%s %d %s", data.city.name, data.temperature, data.condition)
                itemView.setOnClickListener {

                    //getHistoryDao().myDelete((data.city.name))

                    listener.onItemClick(data.city.name,adapterPosition)
                    Toast.makeText(
                        itemView.context,
                        "on click: ${data.city.name}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }*/
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view),
    ItemTouchHelperViewHolder {
        fun bind(data: DataPOD) {
            ItemsBinding.bind(itemView).apply {
                datePicker.text = data.date
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }



/*    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }*/
}
