package com.example.employeemanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.example.employeemanager.R
import com.example.employeemanager.data.Employee
import com.example.employeemanager.databinding.ItemEmployeeBinding
import java.io.File

class EmployeeListAdapter(
    private val onItemClick: (Employee) -> Unit,
    private val onItemSwipe: ((Employee) -> Unit)? = null
) : ListAdapter<Employee, EmployeeListAdapter.EmployeeViewHolder>(EmployeeDiffCallback()) {

    inner class EmployeeViewHolder(val binding: ItemEmployeeBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee) {
            if (!employee.imagePath.isNullOrEmpty()) {
                binding.ivEmployee.load(File(employee.imagePath)) {
                    placeholder(R.drawable.ic_person)
                    error(R.drawable.ic_person)
                    crossfade(true)
                }
            } else {
                binding.ivEmployee.setImageResource(R.drawable.ic_person)
            }
            binding.tvName.text = "${employee.firstName} ${employee.lastName}"
            binding.tvDesignation.text = employee.designation
            binding.root.setOnClickListener { onItemClick(employee) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getEmployeeAt(position: Int): Employee = getItem(position)
}

class EmployeeDiffCallback : DiffUtil.ItemCallback<Employee>() {
    override fun areItemsTheSame(oldItem: Employee, newItem: Employee) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Employee, newItem: Employee) = oldItem == newItem
}