package com.example.employeemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY firstName ASC")
    fun getAllEmployees(): LiveData<List<Employee>>

    @Query("SELECT * FROM employees WHERE firstName LIKE :query OR lastName LIKE :query OR designation LIKE :query ORDER BY firstName ASC")
    fun searchEmployees(query: String): LiveData<List<Employee>>

    @Query("SELECT * FROM employees WHERE email = :email OR phone = :phone LIMIT 1")
    suspend fun findByEmailOrPhone(email: String, phone: String): Employee?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)

    @Update
    suspend fun updateEmployee(employee: Employee)

    @Delete
    suspend fun deleteEmployee(employee: Employee)

    @Query("SELECT * FROM employees WHERE id = :employeeId LIMIT 1")
    suspend fun getEmployeeById(employeeId: Int): Employee?
}