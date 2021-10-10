package com.example.picturesoftheday.view.favoritePOD

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.CustomRowItemBinding
import com.example.picturesoftheday.databinding.FragmentFavoritePicturesBinding
import com.example.picturesoftheday.model.EntityPictures
import com.example.picturesoftheday.viewmodel.PicturesViewModel

class FragmentFavoritePictures : Fragment() {
    private var _binding: FragmentFavoritePicturesBinding? = null
    private val binding: FragmentFavoritePicturesBinding
        get() = _binding!!
    lateinit var itemTouchHelper: ItemTouchHelper
    var isRemove:Boolean = false
    companion object {
        fun newInstance() = FragmentFavoritePictures()
    }

    private val picturesViewModel: PicturesViewModel by lazy {
        ViewModelProvider(this).get(PicturesViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritePicturesBinding.inflate(inflater, container, false)
        val adapter = listAdapter()
        binding.remove.setOnClickListener { isRemove = !isRemove
            Log.i("MyTag", isRemove.toString())
            listAdapter()
            adapter.notifyDataSetChanged();
             }
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)
        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        picturesViewModel.readAllData.observe(viewLifecycleOwner, Observer { list ->
            adapter.setData(list)
        })
        return binding.root
    }

    private fun listAdapter() = ListAdapter(
        object : OnListItemClickListener {
                override fun onItemClick(entityPictures: EntityPictures) {}
            }, object : ListAdapter.OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }

        },
        picturesViewModel, isRemove
    )
}
class ItemTouchHelperCallback(private val adapter: ListAdapter) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(
            dragFlags,
            swipeFlags
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
            itemViewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
        itemViewHolder.onItemClear()
    }
}
