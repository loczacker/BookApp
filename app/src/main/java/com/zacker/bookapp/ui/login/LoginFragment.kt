package com.zacker.bookapp.ui.login

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.DialogForgotPasswordBinding
import com.zacker.bookapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        setUpObserver()
        changeColorButton()
    }

    private fun listener() {
        binding.btnLogin.setOnClickListener {
            val email : String = binding.edEmail.text.toString()
            val pass: String = binding.edPassword.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.login(email, pass)
            } else {
                binding.btnLogin.visibility = View.VISIBLE
            }
        }
        binding.tvRegisterLogin.setOnClickListener {
            viewModel.showRegister()
        }
        binding.tvForgotPassword.setOnClickListener {
            forgotPassword()
        }
    }

    private fun setUpObserver() {
        viewModel.resultData.observe(viewLifecycleOwner) {
            if (it == true) {
                NavHostFragment.findNavController(this).navigate(R.id.login_to_home, null)
            }
        }
        viewModel.startRegister.observe(viewLifecycleOwner) {
            if (it) {
                NavHostFragment.findNavController(this).navigate(R.id.login_to_register, null)
            }
        }
        viewModel.showToast.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeColorButton() {
        binding.edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edEmail.text!!.length
                    and binding.edPassword.text!!.length >0
                ) {
                    binding.btnLogin.setBackgroundResource(R.drawable.buttonblue)
                    binding.btnLogin.isEnabled = true
                } else {
                    binding.btnLogin.setBackgroundResource(R.drawable.button_white)
                    binding.btnLogin.isEnabled = false
                }
            }
        })
        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edEmail.text!!.length and binding.edPassword.text!!.length > 0) {
                    binding.btnLogin.setBackgroundResource(R.drawable.buttonblue)
                    binding.btnLogin.isEnabled = true
                } else {
                    binding.btnLogin.setBackgroundResource(R.drawable.button_white)
                    binding.btnLogin.isEnabled = false
                }
            }
        })
    }

    private fun forgotPassword() {
        val dialogBinding: DialogForgotPasswordBinding =
            DialogForgotPasswordBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.apply {
            window?.setLayout(layoutParams.width, layoutParams.height)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
        }
        dialogBinding.imCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnOk.setOnClickListener {
            val forgotPass = dialogBinding.etEmail.text
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.sendPasswordResetEmail(forgotPass.toString())
                .addOnSuccessListener {
                    dialogBinding.etEmail.clearFocus()
                    dialogBinding.etEmail.text.clear()
                    Toast.makeText(activity, "Kiểm tra email", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Thử lại", Toast.LENGTH_SHORT).show()
                }
        }
        dialog.show()
    }

}
