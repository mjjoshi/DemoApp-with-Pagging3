package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.CollectionAdapter
import com.example.myapplication.adapter.CollectionPagerAdapter
import com.example.myapplication.databinding.FragmentCollecationListBinding
import com.example.myapplication.databinding.FragmentCollecationPagingBinding
import com.example.myapplication.interfaces.OnItemClickListener
import com.example.myapplication.model.collections.CollectionListResponseItem
import com.example.myapplication.utils.Appconstants
import com.example.myapplication.utils.setNavigation
import com.example.myapplication.utils.setToolbartitle
import com.example.myapplication.viewmodel.CollectionViewModel
import com.example.myapplication.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CollectionPagingFragment : Fragment() {

    private lateinit var viewModelCollection: CollectionViewModel
    private lateinit var binding: FragmentCollecationPagingBinding
    private lateinit var collectionAdapter: CollectionPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollecationPagingBinding.inflate(inflater)
        setToolbartitle(getString(R.string.str_collection_list))
        binding.lifecycleOwner = this
        initializeViewModel()
        setAdapterData()
        listenToViewModel()
        return binding.root
    }


    private fun initializeViewModel() {
        val factory = ViewModelFactory(requireActivity().application)
        viewModelCollection = ViewModelProvider(this, factory).get(CollectionViewModel::class.java)
    }

    private fun setAdapterData() {
        collectionAdapter = CollectionPagerAdapter(object : OnItemClickListener {
            override fun onItemClick(item: CollectionListResponseItem) {
                val bundle = Bundle()
                bundle.putString(Appconstants.COLLECATION_ID, item.id)
                setNavigation(
                    bundle = bundle,
                    id = R.id.action_collecationListFragment_to_photoGridFragment
                )
            }
        })
        binding.apply {
            viewModel = viewModelCollection
            rvCollectionList.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = collectionAdapter
            }
        }

    }

    private fun listenToViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModelCollection.getAllCollections.collectLatest { response ->
                binding.apply {
                    pbLoadingC.isVisible = false
                    rvCollectionList.isVisible = true
                }
                collectionAdapter.submitData(response)
            }
        }

    }
}