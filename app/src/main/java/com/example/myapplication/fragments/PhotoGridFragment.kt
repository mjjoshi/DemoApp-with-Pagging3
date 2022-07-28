package com.example.myapplication.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.PhotosAdapter
import com.example.myapplication.databinding.FragmentPhotoGridBinding
import com.example.myapplication.interfaces.OnItemClickPhotoListener
import com.example.myapplication.model.photoGrid.PhotoGrideResponseItem
import com.example.myapplication.utils.*
import com.example.myapplication.viewmodel.CollectionViewModel
import com.example.myapplication.viewmodel.ViewModelFactory


class PhotoGridFragment : Fragment() {

    private lateinit var viewModelCollection: CollectionViewModel
    lateinit var binding: FragmentPhotoGridBinding
    private var collecation_id: String = ""
    lateinit var photoGridAdapter: PhotosAdapter
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    private var page: Int = 1
    val photosListData = ArrayList<PhotoGrideResponseItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoGridBinding.inflate(inflater)
        setToolbartitle(getString(R.string.str_photo_list))
        initializeViewModel()
        binding.lifecycleOwner = this
        binding.viewModel = viewModelCollection
        getArgument()
        listenToViewModel()
        setAdapterData()
        return binding.root
    }

    private fun getArgument() {
        collecation_id = arguments?.getString(Appconstants.COLLECATION_ID, "").toString()
        viewModelCollection.getPhotos(collecation_id, page)
    }

    private fun initializeViewModel() {
        val factory = ViewModelFactory(requireActivity().application)
        viewModelCollection = ViewModelProvider(this, factory).get(CollectionViewModel::class.java)
    }

    private fun listenToViewModel() {
        viewModelCollection.photos.observe(requireActivity()) {
            it?.let {
                if (it.isNotEmpty()) {
                    photosListData.addAll(it)
                    photoGridAdapter.notifyDataSetChanged()

                } else {
                    hideViews()
                }


            }
        }
    }

    private fun hideViews() {
        binding.txtNoData.visiblityHideShow(true)
        binding.rvPhotoList.visiblityHideShow(false)
        binding.pbLoading.visiblityHideShow(false)
    }

    private fun setAdapterData() {
        photoGridAdapter = PhotosAdapter(photosListData,object : OnItemClickPhotoListener {
            override fun onItemphotoClick(item: PhotoGrideResponseItem) {
                val bundle = Bundle()
                bundle.putString(Appconstants.PHOTO_URL, item.user.profile_image.large)
                setNavigation(
                    bundle = bundle,
                    id = R.id.action_photoGridFragment_to_showPhotoFragment
                )

            }
        })
        binding.apply {
            viewModel = viewModelCollection
            rvPhotoList.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = photoGridAdapter
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchItem.actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView!!.maxWidth = Integer.MAX_VALUE
        searchView!!.setOnCloseListener {
            page = 1
            photosListData.clear()
            viewModelCollection.getPhotos(collecation_id, page)
            false
        }
        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    photoGridAdapter.filter.filter(newText)
                } else {
//                    page = 1
//                    viewModelCollection.getPhotos(collecation_id, page)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                photoGridAdapter.filter.filter(query)
                return true
            }
        }
        searchView!!.setOnQueryTextListener(queryTextListener)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search).isVisible = true
        menu.findItem(R.id.action_logout).isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search ->
                return true
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun setPagination() {
//        binding.nestedSv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
//                page++
//                binding.pbLoading.visiblityHideShow(true)
//                if (page > Appconstants.LIMIT) {
//                    requireContext().toast(getString(R.string.str_data_loaded))
//                    binding.pbLoading.visiblityHideShow(false)
//                } else {
//                    viewModelCollection.getPhotos(collecation_id, page)
//                }
//
//            }
//        })
//    }


}