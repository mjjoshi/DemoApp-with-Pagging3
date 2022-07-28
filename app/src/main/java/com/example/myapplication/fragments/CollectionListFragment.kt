package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.CollectionAdapter
import com.example.myapplication.databinding.FragmentCollecationListBinding
import com.example.myapplication.interfaces.OnItemClickListener
import com.example.myapplication.model.collections.CollectionListResponseItem
import com.example.myapplication.utils.*
import com.example.myapplication.utils.Appconstants.COLLECATION_ID
import com.example.myapplication.viewmodel.CollectionViewModel
import com.example.myapplication.viewmodel.ViewModelFactory


class CollectionListFragment : Fragment() {

    private lateinit var viewModelCollection: CollectionViewModel
    private lateinit var binding: FragmentCollecationListBinding
    private lateinit var collectionAdapter: CollectionAdapter
    private var page: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollecationListBinding.inflate(inflater)
        setToolbartitle(getString(R.string.str_collection_list))
        binding.lifecycleOwner = this
        initializeViewModel()
        viewModelCollection.getCollections(page)
        setAdapterData()
        listenToViewModel()
        setPagination()
        return binding.root
    }


    private fun initializeViewModel() {
        val factory = ViewModelFactory(requireActivity().application)
        viewModelCollection = ViewModelProvider(this, factory).get(CollectionViewModel::class.java)
    }

    private fun setAdapterData() {
        collectionAdapter = CollectionAdapter(object : OnItemClickListener {
            override fun onItemClick(item: CollectionListResponseItem) {
                val bundle = Bundle()
                bundle.putString(COLLECATION_ID, item.id)
//                setNavigation(
//                    bundle = bundle,
//                    id = R.id.action_collecationListFragment_to_photoGridFragment
//                )
            }
        })
        binding.apply {
            viewModel = viewModelCollection
            collectionList.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = collectionAdapter
            }
        }

    }

    private fun listenToViewModel() {
        viewModelCollection.collections.observe(requireActivity()) {
            it?.let {
                collectionAdapter.setData(it as ArrayList<CollectionListResponseItem>)

            }
        }
    }

    private fun setPagination() {
        binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                page++
                binding.idPBLoading.visiblityHideShow(true)
                if (page > Appconstants.LIMIT) {
                    requireContext().toast(getString(R.string.str_data_loaded))
                    binding.idPBLoading.visiblityHideShow(false)
                } else {
                    viewModelCollection.getCollections(page)
                }

            }
        })
    }


}