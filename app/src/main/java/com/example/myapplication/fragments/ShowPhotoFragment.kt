package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentShowPhotoBinding
import com.example.myapplication.utils.Appconstants
import com.example.myapplication.utils.loadImage
import com.example.myapplication.utils.setToolbartitle



class ShowPhotoFragment : Fragment() {

    lateinit var binding: FragmentShowPhotoBinding
    private var photo_url: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowPhotoBinding.inflate(inflater)
        setToolbartitle(getString(R.string.str_selected_photo))
        getArgument()
        return binding.root
    }

    fun getArgument() {
        photo_url = arguments?.getString(Appconstants.PHOTO_URL, "").toString()
        binding.imgShowPic.loadImage(photo_url)
    }

}