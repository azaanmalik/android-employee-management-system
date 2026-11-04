package com.example.employeemanager.ui

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import coil.load
import com.example.employeemanager.data.Employee
import com.example.employeemanager.databinding.ActivityAddEditEmployeeBinding
import com.example.employeemanager.util.ValidationUtils
import com.example.employeemanager.viewmodel.EmployeeViewModel
import java.io.File
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddEditEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditEmployeeBinding
    private val viewModel: EmployeeViewModel by viewModels()
    private var currentEmployee: Employee? = null
    private var initialFields: Employee? = null
    private var isSaving = false
    private var imagePath: String? = null
    private var tempPhotoFile: File? = null

    // Gallery picker
    private val pickGalleryImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val dest = File(filesDir, "emp_${System.currentTimeMillis()}.jpg")
            contentResolver.openInputStream(uri)?.use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            }
            imagePath = dest.absolutePath
            binding.ivEmployee.load(dest) {
                placeholder(com.example.employeemanager.R.drawable.ic_person)
                error(com.example.employeemanager.R.drawable.ic_person)
                crossfade(true)
            }
        }
    }

    // Camera
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val photoFile = tempPhotoFile
        if (success && photoFile != null && photoFile.exists()) {
            imagePath = photoFile.absolutePath
            binding.ivEmployee.load(photoFile) {
                placeholder(com.example.employeemanager.R.drawable.ic_person)
                error(com.example.employeemanager.R.drawable.ic_person)
                crossfade(true)
            }
        }
        tempPhotoFile = null // reset to avoid accidental reuse
    }

    private fun showImagePickerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Set Employee Photo")
            .setItems(arrayOf("Take Photo", "Choose from Gallery")) { _, which ->
                when (which) {
                    0 -> {
                        val file = File(filesDir, "emp_${System.currentTimeMillis()}.jpg")
                        tempPhotoFile = file
                        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
                        takePhoto.launch(uri)
                    }
                    1 -> pickGalleryImage.launch("image/*")
                }
            }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivEmployee.setOnClickListener { showImagePickerDialog() }
        binding.ivAddOverlay.setOnClickListener { showImagePickerDialog() }
        val employeeId = intent.getIntExtra("employeeId", -1)
        if (employeeId != -1) {
            viewModel.getEmployeeById(employeeId) { employee ->
                employee?.let {
                    currentEmployee = it
                    initialFields = it.copy()
                    populateFields(it)
                    imagePath = it.imagePath
                    if (!imagePath.isNullOrEmpty()) {
                        binding.ivEmployee.load(File(imagePath!!)) {
                            placeholder(com.example.employeemanager.R.drawable.ic_person)
                            error(com.example.employeemanager.R.drawable.ic_person)
                            crossfade(true)
                        }
                    } else {
                        binding.ivEmployee.setImageResource(com.example.employeemanager.R.drawable.ic_person)
                    }
                    binding.btnDelete.visibility = View.VISIBLE
                }
            }
        } else {
            binding.btnDelete.visibility = View.GONE
        }

        binding.btnSave.setOnClickListener {
            if (isSaving) return@setOnClickListener
            if (validateInputs()) {
                val employee = Employee(
                    id = currentEmployee?.id ?: 0,
                    firstName = binding.etFirstName.text.toString().trim(),
                    lastName = binding.etLastName.text.toString().trim(),
                    email = binding.etEmail.text.toString().trim(),
                    phone = binding.etPhone.text.toString().trim(),
                    address = binding.etAddress.text.toString().trim(),
                    designation = binding.etDesignation.text.toString().trim(),
                    salary = binding.etSalary.text.toString().toDouble(),
                    imagePath = imagePath
                )
                setUiLoading(true)
                if (currentEmployee == null) {
                    viewModel.insert(employee) { success, error ->
                        setUiLoading(false)
                        if (success) {
                            Toast.makeText(this, "Employee added.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, error ?: "Error adding employee.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    viewModel.update(employee) { success, error ->
                        setUiLoading(false)
                        if (success) {
                            Toast.makeText(this, "Employee updated.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, error ?: "Error updating employee.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            if (isSaving) return@setOnClickListener
            AlertDialog.Builder(this)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Yes") { _, _ ->
                    setUiLoading(true)
                    currentEmployee?.let {
                        viewModel.delete(it)
                        Toast.makeText(this, "Employee deleted.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onBackPressed() {
        if (hasUnsavedChanges()) {
            AlertDialog.Builder(this)
                .setTitle("Discard changes?")
                .setMessage("You have unsaved changes. Are you sure you want to leave?")
                .setPositiveButton("Discard") { _, _ -> super.onBackPressed() }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }

    private fun populateFields(employee: Employee) {
        binding.etFirstName.setText(employee.firstName)
        binding.etLastName.setText(employee.lastName)
        binding.etEmail.setText(employee.email)
        binding.etPhone.setText(employee.phone)
        binding.etAddress.setText(employee.address)
        binding.etDesignation.setText(employee.designation)
        binding.etSalary.setText(employee.salary.toString())
    }

    private fun collectCurrentFields(): Employee = Employee(
        id = currentEmployee?.id ?: 0,
        firstName = binding.etFirstName.text.toString().trim(),
        lastName = binding.etLastName.text.toString().trim(),
        email = binding.etEmail.text.toString().trim(),
        phone = binding.etPhone.text.toString().trim(),
        address = binding.etAddress.text.toString().trim(),
        designation = binding.etDesignation.text.toString().trim(),
        salary = binding.etSalary.text.toString().toDoubleOrNull() ?: 0.0,
        imagePath = imagePath
    )

    private fun hasUnsavedChanges(): Boolean {
        if (currentEmployee == null) {
            return collectCurrentFields().let {
                it.firstName.isNotBlank() || it.lastName.isNotBlank() || it.email.isNotBlank() ||
                        it.phone.isNotBlank() || it.address.isNotBlank() || it.designation.isNotBlank() || it.salary != 0.0 || !imagePath.isNullOrEmpty()
            }
        }
        return collectCurrentFields() != initialFields
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        with(binding) {
            if (!ValidationUtils.isValidName(etFirstName.text.toString())) {
                etFirstName.error = "Enter a valid first name (letters, 2-50 chars)"
                isValid = false
            }
            if (!ValidationUtils.isValidName(etLastName.text.toString())) {
                etLastName.error = "Enter a valid last name (letters, 2-50 chars)"
                isValid = false
            }
            if (!ValidationUtils.isValidEmail(etEmail.text.toString())) {
                etEmail.error = "Enter a valid email address"
                isValid = false
            }
            if (!ValidationUtils.isValidPhone(etPhone.text.toString())) {
                etPhone.error = "Enter a valid phone (10-15 digits, optional +)"
                isValid = false
            }
            if (!ValidationUtils.isValidAddress(etAddress.text.toString())) {
                etAddress.error = "Enter a valid address (min 5 chars)"
                isValid = false
            }
            if (!ValidationUtils.isValidDesignation(etDesignation.text.toString())) {
                etDesignation.error = "Enter a valid designation (2-40 chars)"
                isValid = false
            }
            if (!ValidationUtils.isValidSalary(etSalary.text.toString())) {
                etSalary.error = "Enter a valid salary (numeric, >0, max 1M)"
                isValid = false
            }
        }
        return isValid
    }

    private fun setUiLoading(loading: Boolean) {
        isSaving = loading
        binding.btnSave.isEnabled = !loading
        binding.btnDelete.isEnabled = !loading
        binding.progressBar?.visibility = if (loading) View.VISIBLE else View.GONE
    }
}