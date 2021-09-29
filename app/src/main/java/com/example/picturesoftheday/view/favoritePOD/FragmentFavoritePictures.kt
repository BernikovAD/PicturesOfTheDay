package com.example.picturesoftheday.view.favoritePOD

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.picturesoftheday.databinding.FragmentFavoritePicturesBinding
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PictureViewModel


class FragmentFavoritePictures : Fragment() , OnClickAdapterItem {
    private var _binding: FragmentFavoritePicturesBinding? = null
    private val binding: FragmentFavoritePicturesBinding
        get() = _binding!!
    val data:MutableList<String?> = ArrayList()
    private val adapter: AdapterRecyclerView by lazy { AdapterRecyclerView() }
    private val viewModel: PictureViewModel by lazy { ViewModelProvider(this).get(PictureViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getAllPictures()


        return binding.root
    }

    companion object {
        fun newInstance() = FragmentFavoritePictures()
    }
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessDB -> {
                binding.recycler.adapter = adapter
                //ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(binding.recycler)
                adapter.setListener(this)
                adapter.setData(appState.dataPOD)
            }


            }
        }

    override fun onItemClick(name: String, position: Int) {
        viewModel.getAllPictures()
    }




  /*  class ItemTouchHelperCallback(private val adapter: AdapterRecyclerView) :
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
}*/
}