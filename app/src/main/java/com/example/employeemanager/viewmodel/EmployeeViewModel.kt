package com.example.employeemanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.employeemanager.data.Employee
import com.example.employeemanager.data.EmployeeDatabase
import com.example.employeemanager.repository.EmployeeRepository
import kotlinx.coroutines.launch

enum class SortOption { NAME, DESIGNATION, SALARY }

class EmployeeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EmployeeRepository
    private val searchQuery = MutableLiveData("")
    private val sortOption = MutableLiveData(SortOption.NAME)

    private var currentSource: LiveData<List<Employee>>? = null

    init {
        val employeeDao = EmployeeDatabase.getDatabase(application).employeeDao()
        repository = EmployeeRepository(employeeDao)
    }

    val allEmployees = MediatorLiveData<List<Employee>>().apply {
        val update: () -> Unit = {
            // Remove the previous source if it exists
            currentSource?.let { removeSource(it) }

            val query = searchQuery.value ?: ""
            val sort = sortOption.value ?: SortOption.NAME
            val source = if (query.isEmpty()) repository.allEmployees else repository.searchEmployees(query)
            currentSource = source

            addSource(source) { list ->
                value = when (sort) {
                    SortOption.NAME -> list.sortedBy { it.firstName }
                    SortOption.DESIGNATION -> list.sortedBy { it.designation }
                    SortOption.SALARY -> list.sortedByDescending { it.salary }
                }
            }
        }
        addSource(searchQuery) { update() }
        addSource(sortOption) { update() }
        update()
    }

    fun setSearchQuery(query: String) { searchQuery.value = query }
    fun setSortOption(option: SortOption) { sortOption.value = option }

    fun insert(employee: Employee, onResult: (Boolean, String?) -> Unit) = viewModelScope.launch {
        val duplicate = repository.findByEmailOrPhone(employee.email, employee.phone)
        if (duplicate != null && duplicate.id != employee.id) {
            onResult(false, "Employee with this email or phone already exists.")
        } else {
            repository.insert(employee)
            onResult(true, null)
        }
    }

    fun update(employee: Employee, onResult: (Boolean, String?) -> Unit) = viewModelScope.launch {
        val duplicate = repository.findByEmailOrPhone(employee.email, employee.phone)
        if (duplicate != null && duplicate.id != employee.id) {
            onResult(false, "Another employee with this email or phone exists.")
        } else {
            repository.update(employee)
            onResult(true, null)
        }
    }

    fun delete(employee: Employee) = viewModelScope.launch {
        repository.delete(employee)
    }

    fun getEmployeeById(id: Int, callback: (Employee?) -> Unit) = viewModelScope.launch {
        callback(repository.getEmployeeById(id))
    }
}