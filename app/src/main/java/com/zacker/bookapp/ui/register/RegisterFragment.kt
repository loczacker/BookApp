package com.zacker.bookapp.ui.register

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
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.DialogForgotPasswordBinding
import com.zacker.bookapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeColorButton()
        observer()
        listener()
    }

    private fun listener() {
        binding.btRes.setOnClickListener {
            val userName: String = binding.edName.text.toString()
            val email: String = binding.edEmail.text.toString()
            val pass: String = binding.edPassword.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty() && userName.isNotEmpty()) {
                viewModel.register(email, pass, userName)
            } else {
                binding.btRes.visibility = View.VISIBLE
            }

        }
        binding.tvLoginRegister.setOnClickListener {
//            NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment, null)
            Navigation.findNavController(requireView()).navigateUp()
        }
        binding.ibRes.setOnClickListener {
//            NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment, null)
            Navigation.findNavController(requireView()).navigateUp()
        }
        binding.tvForgot.setOnClickListener {
            forgotPassword()
        }
    }

    private fun changeColorButton() {
        binding.edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edName.text!!.isNotEmpty()
                    && binding.edEmail.text!!.isNotEmpty()
                    && binding.edPassword.text!!.isNotEmpty()
                    && binding.cb1.isChecked
                ) {
                    binding.btRes.setBackgroundResource(R.drawable.buttonblue)
                    binding.btRes.isEnabled = true
                } else {
                    binding.btRes.setBackgroundResource(R.drawable.button_white)
                    binding.btRes.isEnabled = false
                }
            }
        })
        binding.edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edName.text!!.isNotEmpty()
                    && binding.edEmail.text!!.isNotEmpty()
                    && binding.edPassword.text!!.isNotEmpty()
                    && binding.cb1.isChecked
                ) {
                    binding.btRes.setBackgroundResource(R.drawable.buttonblue)
                    binding.btRes.isEnabled = true
                } else {
                    binding.btRes.setBackgroundResource(R.drawable.button_white)
                    binding.btRes.isEnabled = false
                }
            }
        })
        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edName.text!!.isNotEmpty()
                    && binding.edEmail.text!!.isNotEmpty()
                    && binding.edPassword.text!!.isNotEmpty()
                    && binding.cb1.isChecked
                ) {
                    binding.btRes.setBackgroundResource(R.drawable.buttonblue)
                    binding.btRes.isEnabled = true
                } else {
                    binding.btRes.setBackgroundResource(R.drawable.button_white)
                    binding.btRes.isEnabled = false
                }
            }
        })
        binding.cb1.setOnCheckedChangeListener { _, _ ->
            if (binding.edName.text!!.isNotEmpty()
                && binding.edEmail.text!!.isNotEmpty()
                && binding.edPassword.text!!.isNotEmpty()
                && binding.cb1.isChecked
            ) {
                binding.btRes.setBackgroundResource(R.drawable.buttonblue)
                binding.btRes.isEnabled = true
            } else {
                binding.btRes.setBackgroundResource(R.drawable.button_white)
                binding.btRes.isEnabled = false
            }
        }
    }

    private fun forgotPassword() {
        val dialogBinding: DialogForgotPasswordBinding =
            DialogForgotPasswordBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
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
            val firebaseAuth = FirebaseAuth.getInstance()
            val forgotPass = dialogBinding.etEmail.text
            firebaseAuth.sendPasswordResetEmail(forgotPass.toString())
                .addOnSuccessListener {
                    dialogBinding.etEmail.clearFocus()
                    dialogBinding.etEmail.text.clear()
                    Toast.makeText(requireContext(), "Kiểm tra email", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Thử lại", Toast.LENGTH_SHORT).show()
                }
        }
        dialog.show()
    }

    private fun observer() {
        viewModel.resultData.observe(viewLifecycleOwner) {
            if (it == true) {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_registerFragment_to_homeFragment, null)
            }
        }
        viewModel.showToast.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), "Đăng kí thành công", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.showToast1.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), "Gmail không hợp lệ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}