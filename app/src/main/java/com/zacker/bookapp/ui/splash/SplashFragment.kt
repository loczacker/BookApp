package com.zacker.bookapp.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.splash.alpha = 0f
        binding.splash.animate().setDuration(1500).alpha(1f).withEndAction {
            viewModel.checkLogin()
        }
        checkUser()
    }

    private fun checkUser() {
        viewModel.checkUser.observe(viewLifecycleOwner) {
            if (it == viewModel.OPEN_LOGIN) {
                NavHostFragment.findNavController(this).navigate(R.id.splash_to_login, null)
            } else {
                NavHostFragment.findNavController(this).navigate(R.id.splash_to_home, null)
            }
//            NavHostFragment.findNavController(this).navigate(R.id.splash_to_login, null)
        }
    }
}