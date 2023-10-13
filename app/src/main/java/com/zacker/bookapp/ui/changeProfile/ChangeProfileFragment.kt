@file:Suppress("DEPRECATION")

package com.zacker.bookapp.ui.changeProfile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentChangeProfileBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ChangeProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentChangeProfileBinding
    private lateinit var viewModel: ChangeProfileViewModel

    private lateinit var firebaseAuth: FirebaseAuth

    private var imageUri: Uri? = null

    private lateinit var progressDialog: ProgressDialog

    private val calendar = Calendar.getInstance()

    private val myFormat = SimpleDateFormat("dd-MM-yyyy", Locale.UK)

    private val CAMERA_PERMISSION_CODE = 101
    private val GALLERY_PERMISSION_CODE = 102
    private val STORAGE_PERMISSION_CODE = 103

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChangeProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ChangeProfileViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        checkPermission()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Vui lòng đợi")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()
        setListeners()
    }

    private fun setListeners(){
        binding.imgCamera.setOnClickListener {
            showImageAttachMenu()
        }
        binding.tvDone.setOnClickListener {
            validateData()
        }
        binding.edBirthdayChange.setOnClickListener {
            showDatePickerDialog()
        }
        binding.imgBackHomeProfile.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    private fun checkPermission() {
        val cameraPermission = Manifest.permission.CAMERA
        val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(requireContext(), cameraPermission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(cameraPermission),
                CAMERA_PERMISSION_CODE
            )
        }

        if (ContextCompat.checkSelfPermission(requireContext(), galleryPermission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(galleryPermission),
                GALLERY_PERMISSION_CODE
            )
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageCamera()
                }
            }
            GALLERY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    picImageGallery()
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val currentDate = binding.edBirthdayChange.text.toString()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        if (currentDate.isNotEmpty()) {
            val selectedDate = dateFormat.parse(currentDate)
            selectedDate?.let {
                calendar.time = it
            }
        }

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedTimestamp = calendar.timeInMillis
                val currentTimestamp = System.currentTimeMillis()
                if (selectedTimestamp <= currentTimestamp) {
                    binding.edBirthdayChange.setText(dateFormat.format(selectedTimestamp))
                } else {
                    Toast.makeText(activity, "Chọn đúng ngày", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }




    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        displayFormat(calendar.timeInMillis)
    }

    private fun displayFormat(timestamp: Long) {
        binding.edBirthdayChange.setText(myFormat.format(timestamp))
    }

    private var name = ""
    private var phone = ""
    private var birthday = ""

    private fun validateData() {
        name = binding.edtChangeName.text.toString().trim()
        phone = binding.edPhoneChange.text.toString().trim()
        birthday = binding.edBirthdayChange.text.toString().trim()

        if (name.isEmpty() && phone.isEmpty() && birthday.isEmpty()) {
            Toast.makeText(activity, "Enter name", Toast.LENGTH_SHORT).show()
        } else {
            if (imageUri == null) {
                updateProfile("")
            } else {
                uploadImage()
            }
        }
    }

    private fun updateProfile(uploadedImageUrl: String) {
        progressDialog.setMessage("Cập nhật thông tin")
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["name"] = name
        hashMap["phone"] = phone
        hashMap["birthday"] = birthday
        if (imageUri != null) {
            hashMap["img"] = uploadedImageUrl
        }
        //update to db
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(activity, "Cập nhật ", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView()).navigateUp()

            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(activity, "Failed to upload profile due ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImage() {
        progressDialog.setMessage("đang tải thông tin")
        progressDialog.show()
        val filePathAndName = "img/" + firebaseAuth.uid
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadImageUrl = "${uriTask.result}"
                updateProfile(uploadImageUrl)

            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(activity, "Failed to upload image due ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val name = "${snapshot.child("name").value}"
                    val img = "${snapshot.child("img").value}"
                    val birthday = "${snapshot.child("birthday").value}"
                    val phone = "${snapshot.child("phone").value}"

                    binding.edtChangeName.setText(name)
                    binding.edPhoneChange.setText(phone)
                    binding.edBirthdayChange.setText(birthday)

                    try {

                        Glide.with(this@ChangeProfileFragment)
                            .load(img)
                            .placeholder(R.drawable.profile)
                            .into(binding.imgAva)

                    } catch (_: Exception) {}
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun showImageAttachMenu() {
        val popupMenu = PopupMenu(activity, binding.imgCamera)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        //handle popup menu item click
        popupMenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
            if (id == 0) {
                pickImageCamera()
            } else if (id == 1) {
                picImageGallery()
            }
            true
        }
        popupMenu.show()
    }


    private fun picImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")
        val resolver = requireActivity().contentResolver
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (imageUri != null) {
                Glide.with(this@ChangeProfileFragment)
                    .load(imageUri)
                    .placeholder(R.drawable.profile)
                    .into(binding.imgAva)
            } else {
                Toast.makeText(requireContext(), "Không thể tải ảnh.", Toast.LENGTH_SHORT).show()
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
        } else {
            Toast.makeText(requireContext(), "Lỗi khi chụp ảnh.", Toast.LENGTH_SHORT).show()
        }
    }


    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            imageUri = data!!.data
            if (imageUri != null) {
                Glide.with(this@ChangeProfileFragment)
                    .load(imageUri)
                    .placeholder(R.drawable.profile)
                    .into(binding.imgAva)
            }
        }
    }
}