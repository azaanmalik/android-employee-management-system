package com.example.employeemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val address: String,
    val designation: String,
    val salary: Double,
    val imagePath: String? = null // <-- Added for employee photo
)