package com.example.employeemanager.repository

import androidx.lifecycle.LiveData
import com.example.employeemanager.data.Employee
import com.example.employeemanager.data.EmployeeDao

class EmployeeRepository(private val employeeDao: EmployeeDao) {
    val allEmployees: LiveData<List<Employee>> = employeeDao.getAllEmployees()

    fun searchEmployees(query: String): LiveData<List<Employee>> =
        employeeDao.searchEmployees("%$query%")

    suspend fun insert(employee: Employee) = employeeDao.insertEmployee(employee)
    suspend fun update(employee: Employee) = employeeDao.updateEmployee(employee)
    suspend fun delete(employee: Employee) = employeeDao.deleteEmployee(employee)
    suspend fun getEmployeeById(id: Int) = employeeDao.getEmployeeById(id)
    suspend fun findByEmailOrPhone(email: String, phone: String) = employeeDao.findByEmailOrPhone(email, phone)
}