package com.example.myapplication.utils

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun Fragment.setNavigation(bundle: Bundle, id: Int) {
    findNavController().navigate(id, bundle)
}

fun Fragment.setToolbartitle(title: String) {
    (requireActivity() as AppCompatActivity).supportActionBar?.title = title

}

fun ImageView.loadImage(url: String) {
    load(url)
}

fun View.visiblityHideShow(isVisible: Boolean = false) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}