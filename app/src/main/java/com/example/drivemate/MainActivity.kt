package com.example.drivemate

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.drivemate.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val viewModel: CarRentalViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find views using ViewBinding
        val etPickup = binding.etPickupLocation
        val etDropOff = binding.etDropoffLocation
        val etPickupDate = binding.etPickupDate
        val etDropOffDate = binding.etDropoffDate
        val btnSearch = binding.btnSearchKayak

        // Disable keyboard input for date fields (ensures only DatePicker is used)
        etPickupDate.isFocusable = false
        etPickupDate.isClickable = true
        etDropOffDate.isFocusable = false
        etDropOffDate.isClickable = true

        // Show DatePicker on Pickup Date field click
        etPickupDate.setOnClickListener {
            showDatePicker { selectedDate ->
                viewModel.pickupDate.value = selectedDate
                etPickupDate.setText(selectedDate) // Update EditText with selected date
            }
        }

        // Show DatePicker on Drop-off Date field click
        etDropOffDate.setOnClickListener {
            showDatePicker { selectedDate ->
                viewModel.dropOffDate.value = selectedDate
                etDropOffDate.setText(selectedDate) // Update EditText with selected date
            }
        }

        // Search button click listener
        btnSearch.setOnClickListener {
            // Update ViewModel with user input
            viewModel.pickupLocation.value = etPickup.text.toString()
            viewModel.dropOffLocation.value = etDropOff.text.toString()

            // Generate the deep link URL
            val deepLinkUrl = viewModel.generateDeepLink()
            if (deepLinkUrl.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            } else {
                // Launch the URL in a browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl))
                startActivity(intent)
            }
        }
    }

    /**
     * Function to show a DatePickerDialog and return the selected date as "YYYY-MM-DD".
     */
    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    .format(Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }.time)
                onDateSelected(formattedDate) // Callback with selected date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
}
