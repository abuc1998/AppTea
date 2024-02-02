// File: EmployeesFragment.kt
package com.example.apptea.ui.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptea.R
import com.example.apptea.databinding.FragmentEmployeesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EmployeesFragment : Fragment(), AddEmployeeDialogFragment.OnEmployeeSavedListener,
    OnEmployeeUpdatedListener {

    private var _binding: FragmentEmployeesBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var employeeAdapter: EmployeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        employeesViewModel = ViewModelProvider(this).get(EmployeesViewModel::class.java)

        _binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access the FloatingActionButton
        val fabAddEmployee = root.findViewById<FloatingActionButton>(R.id.fabAddEmployee)

        // Set up RecyclerView
        recyclerView = root.findViewById(R.id.recyclerView)
        employeeAdapter = EmployeeAdapter(emptyList()) // Initialize the adapter
        recyclerView.adapter = employeeAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe changes in the employee list and update the adapter
        employeesViewModel.employeeList.observe(viewLifecycleOwner, Observer { employees ->
            employeeAdapter.updateData(employees)
            employeeAdapter.notifyDataSetChanged()
        })

        // Fetch employees when the fragment is created
        employeesViewModel.fetchEmployees()

        fabAddEmployee.setOnClickListener {
            // Show the FormDialogFragment when FAB is clicked
            val formDialog = AddEmployeeDialogFragment()
            formDialog.employeeSavedListener = this
            formDialog.show(
                childFragmentManager,
                AddEmployeeDialogFragment::class.java.simpleName
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEmployeeSaved() {
        // Handle the logic to refresh your fragment
        // For example, reload the data or re-fetch the records
        employeesViewModel.fetchEmployees()
    }

    override fun onEmployeeUpdated() {
        // Not needed here, as the observer in onCreateView will automatically update the adapter
    }
}
