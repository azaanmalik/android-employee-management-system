package com.example.employeemanager.util

object ValidationUtils {
    fun isValidName(name: String): Boolean = name.trim().matches(Regex("^[A-Za-z][A-Za-z\\s'-]{1,48}[A-Za-z]$"))

    fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPhone(phone: String): Boolean =
        phone.trim().matches(Regex("^\\+?[0-9]{10,15}\$"))

    fun isValidAddress(address: String): Boolean = address.length in 5..100

    fun isValidDesignation(designation: String): Boolean = designation.length in 2..30

    fun isValidSalary(salary: String): Boolean =
        salary.toDoubleOrNull()?.let { it > 0 && it < 1_000_000 } ?: false
}