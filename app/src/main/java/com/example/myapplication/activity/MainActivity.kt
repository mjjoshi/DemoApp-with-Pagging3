package com.example.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.toast
import com.example.myapplication.viewmodel.LoginViewmodel
import com.example.myapplication.viewmodel.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: LoginViewmodel

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initializeViewModel()
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel
        observeData()
        setListener()

    }

    private fun setListener() {
        binding.btnSignin.setOnClickListener {
            signInButtonClick()

        }
    }

    fun signInButtonClick() {
        when {
            Firebase.auth.currentUser != null -> { //first check user already signing or not.
                viewModel.name.value = Firebase.auth.currentUser!!.displayName.toString()
                toast(getString(R.string.str_sign_in))
                startActivity(Intent(this@MainActivity, ListingActivity::class.java))
            }
            else -> {
                startGoogleSignR.launch(viewModel.googleSignInClient.signInIntent)

            }
        }
    }

    private fun observeData() {
        viewModel.currentUser.observe(this) {
            it?.let {
                viewModel.name.value = it.displayName

            }
        }
    }

    private fun initializeViewModel() {
        val factory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(LoginViewmodel::class.java)
    }

    private val startGoogleSignR =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    viewModel.firebaseAuthWithGoogle(account.idToken!!)
                    toast(message = getString(R.string.str_sign_in))
                    startActivity(Intent(this@MainActivity, ListingActivity::class.java))
                } catch (e: ApiException) {
                    toast(e.message.toString())

                }

            }
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        invalidateOptionsMenu()
        menu.findItem(R.id.action_search).isVisible = false
        menu.findItem(R.id.action_logout).isVisible = true
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout ->
                exitAppDialog()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitAppDialog() {
        val builder1 = AlertDialog.Builder(this@MainActivity)
        builder1.setMessage(getString(R.string.str_logout_message))
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            getString(R.string.str_yes)
        ) { dialog, _ ->
            viewModel.logout()
            toast(getString(R.string.str_signout_msg))
            dialog.dismiss()
        }
        builder1.setNegativeButton(
            getString(R.string.str_no)
        ) { dialog, _ ->
            dialog.dismiss()
        }
        val alert11 = builder1.create()
        alert11.show()
    }


}