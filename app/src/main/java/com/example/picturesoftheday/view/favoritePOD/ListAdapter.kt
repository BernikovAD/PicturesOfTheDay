package com.example.picturesoftheday.view.favoritePOD

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.picturesoftheday.databinding.CustomRowItemBinding
import com.example.picturesoftheday.databinding.HeaderItemBinding
import com.example.picturesoftheday.model.EntityPictures
import com.example.picturesoftheday.viewmodel.PicturesViewModel


class ListAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var dragListener: OnStartDragListener,
    private val viewModel: PicturesViewModel,
    var isRemove: Boolean
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {
    private var picturesList: MutableList<Pair<EntityPictures, Boolean>> = ArrayList()

    companion object {
        private const val TYPE_ENTITY = 0
        private const val TYPE_HEADER = 1
    }

    //var isRemove: Boolean = false

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    override fun getItemCount(): Int {
        return picturesList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return TYPE_HEADER
        return TYPE_ENTITY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return when (viewType) {
            TYPE_ENTITY -> {
                val binding: CustomRowItemBinding =
                    CustomRowItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MyViewHolder(binding.root)
            }
            else -> {
                val binding: HeaderItemBinding =
                    HeaderItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                HeaderViewHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder).bind(picturesList[position])
    }


    fun setData(entityPictures: List<EntityPictures>) {
        entityPictures.forEach {
            picturesList.add(Pair(it, false))
        }
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        picturesList.removeAt(fromPosition).apply {
            picturesList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        viewModel.deleteUser(picturesList[position].first)
        picturesList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(pair: Pair<EntityPictures, Boolean>) {
            HeaderItemBinding.bind(itemView).apply {
                root.setOnClickListener {
                    onListItemClickListener.onItemClick(pair.first)
                }
            }
        }
    }

    inner class MyViewHolder(itemView: View) : BaseViewHolder(itemView), ItemTouchHelperViewHolder {
        override fun bind(pair: Pair<EntityPictures, Boolean>) {
            CustomRowItemBinding.bind(itemView).apply {
                textDatePictureOfTheDay.text = pair.first.date
                miniPictureUrl.load(pair.first.url)
                miniPictureUrl.visibility = if (pair.second) View.GONE else View.VISIBLE
                pictureUrl.load(pair.first.url)
                pictureUrl.visibility = if (pair.second) View.VISIBLE else View.GONE
                constraintItem.setOnClickListener {
                    toggleText()
                    onListItemClickListener.onItemClick(pair.first)
                }
                Log.i("MyTag", isRemove.toString())
                dragHandleImageView.visibility = if (isRemove) View.VISIBLE else View.INVISIBLE

            }
        }

        private fun toggleText() {
            picturesList[layoutPosition] = picturesList[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }


}