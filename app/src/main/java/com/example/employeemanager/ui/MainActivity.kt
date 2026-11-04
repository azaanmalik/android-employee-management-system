package com.example.employeemanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.employeemanager.R
import com.example.employeemanager.databinding.ActivityMainBinding
import com.example.employeemanager.viewmodel.EmployeeViewModel
import com.example.employeemanager.viewmodel.SortOption
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: EmployeeViewModel by viewModels()
    private lateinit var adapter: EmployeeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) // Ensure the menu is visible

        adapter = EmployeeListAdapter(
            onItemClick = { employee ->
                val intent = Intent(this, AddEditEmployeeActivity::class.java)
                intent.putExtra("employeeId", employee.id)
                startActivity(intent)
            }
        )

        binding.rvEmployees.layoutManager = LinearLayoutManager(this)
        binding.rvEmployees.adapter = adapter

        // Swipe to delete with undo
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val position = vh.adapterPosition
                val employee = adapter.getEmployeeAt(position)
                showMainProgress(true)
                viewModel.delete(employee)
                Snackbar.make(binding.root, "Employee deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        showMainProgress(true)
                        // Provide a result callback to hide loading
                        viewModel.insert(employee) { _, _ -> showMainProgress(false) }
                    }
                    .addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            showMainProgress(false)
                        }
                    })
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvEmployees)

        viewModel.allEmployees.observe(this) { employees ->
            adapter.submitList(employees)
            binding.tvEmpty.visibility = if (employees.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditEmployeeActivity::class.java)
            startActivity(intent)
        }
    }

    // ProgressBar show/hide logic for main activity
    private fun showMainProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.fabAdd.isEnabled = !show
        binding.rvEmployees.isEnabled = !show
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = "Search employees"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query?.trim() ?: "")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText?.trim() ?: "")
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_name -> viewModel.setSortOption(SortOption.NAME)
            R.id.action_sort_designation -> viewModel.setSortOption(SortOption.DESIGNATION)
            R.id.action_sort_salary -> viewModel.setSortOption(SortOption.SALARY)
        }
        return super.onOptionsItemSelected(item)
    }
}