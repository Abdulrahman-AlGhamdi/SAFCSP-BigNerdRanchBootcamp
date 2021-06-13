package com.ss.restaloca.details.reserve

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.ss.restaloca.MainActivity
import com.ss.restaloca.R
import com.ss.restaloca.database.RestaLocaDatabase
import com.ss.restaloca.databinding.FragmentReservationBinding
import com.ss.restaloca.databinding.FragmentReserveBinding
import com.ss.restaloca.details.DetailsViewModel
import com.ss.restaloca.reservation.ReservationModel
import com.ss.restaloca.reservation.ReservationProviderFactory
import com.ss.restaloca.reservation.ReservationRepository
import com.ss.restaloca.reservation.ReservationViewModel
import yuku.ambilwarna.AmbilWarnaDialog
import java.util.*

class reserveFragment : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentReserveBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var reservationViewModel: ReservationViewModel
    private val argument by navArgs<reserveFragmentArgs>()
    private var defaultColor: Int = 0
    private var dayInNumber = 0
    private var dateChosen = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReserveBinding.inflate(inflater, container, false)

        // Initialize views and variables
        init()
        setUpDatePickerDialog()
        setUpColorPickerDialog()
        checkData()

        return binding.root
    }

    private fun init() {
        // Initialize instance of restaLoca view model
        detailsViewModel = (activity as MainActivity).detailsViewModel

        // Initialize instance of reservation view model
        val repository = ReservationRepository(RestaLocaDatabase(requireContext()))
        val factory = ReservationProviderFactory(repository)
        reservationViewModel = ViewModelProvider(this, factory)[ReservationViewModel::class.java]

        // default color for the color picker dialog
        defaultColor = resources.getColor(R.color.colorPrimaryDark, null)
    }

    private fun setUpDatePickerDialog() {
        // Set the current date
        val calendar = Calendar.getInstance()
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val date = DatePickerDialog(requireContext(), this, year, month, day)

        // Set the minimum and maximum
        date.datePicker.apply {
            minDate = System.currentTimeMillis()
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_MONTH, 9)
            maxDate = calendar.time.time
        }

        // Show the date picker dialog
        binding.datePicker.setOnClickListener {
            date.show()
        }
    }

    private fun setUpColorPickerDialog() {
        binding.colorPicker.setOnClickListener {
            AmbilWarnaDialog(
                requireContext(), defaultColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        defaultColor = color
                    }
                }).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        // Get the date chosen from the dialog
        val date = GregorianCalendar(year, month, dayOfMonth).time

        // Format the date to get the specific day
        dateChosen = DateFormat.format("EEEE, dd/MM/yyyy", date).toString()

        // Change the day value to its representative number
        when (dateChosen) {
            getString(R.string.monday) -> dayInNumber = 0
            getString(R.string.tuesday) -> dayInNumber = 1
            getString(R.string.wednesday) -> dayInNumber = 2
            getString(R.string.thursday) -> dayInNumber = 3
            getString(R.string.friday) -> dayInNumber = 4
            getString(R.string.saturday) -> dayInNumber = 5
            getString(R.string.sunday) -> dayInNumber = 6
        }
    }

    private fun checkData() {

        if (argument.selectedReservation != null) {
            binding.note.setText(argument.selectedReservation?.reservationNote)
            binding.description.setText(argument.selectedReservation?.reservationDescription)
        }

        binding.reserve.setOnClickListener {
            when {
                binding.note.text.toString().isBlank() -> Snackbar.make(
                    requireDialog().window!!.decorView,
                    getString(R.string.must_write_note),
                    Snackbar.LENGTH_SHORT
                ).show()

                dateChosen.isBlank() -> Snackbar.make(
                    requireDialog().window!!.decorView,
                    getString(R.string.must_pick_date),
                    Snackbar.LENGTH_SHORT
                ).show()

                else -> addOrUpdateReservation()
            }
        }
    }

    private fun addOrUpdateReservation() {
        if (argument.selectedReservation != null) {
            reservationViewModel.addReservation(
                ReservationModel(
                    argument.selectedReservation!!.businessId,
                    argument.selectedReservation!!.businessCategory,
                    argument.selectedReservation!!.businessLatitude,
                    argument.selectedReservation!!.businessLongitude,
                    argument.selectedReservation!!.businessImageUrl,
                    argument.selectedReservation!!.businessAddress,
                    argument.selectedReservation!!.businessName,
                    argument.selectedReservation!!.businessPhone,
                    argument.selectedReservation!!.businessRating,
                    argument.selectedReservation!!.businessReviewCount,
                    binding.note.text.toString(),
                    binding.description.text.toString(),
                    dateChosen,
                    defaultColor,
                    true
                )
            )

            binding.group.visibility = View.INVISIBLE
            binding.loading.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 1500)
        } else {
            detailsViewModel.informationLiveData.observe(viewLifecycleOwner, { information ->
                reservationViewModel.addReservation(
                    ReservationModel(
                        information.id,
                        information.categories.first().title,
                        information.coordinates.latitude,
                        information.coordinates.longitude,
                        information.image_url,
                        information.location.address1,
                        information.name,
                        information.phone.toString(),
                        information.rating,
                        information.review_count,
                        binding.note.text.toString(),
                        binding.description.text.toString(),
                        dateChosen,
                        defaultColor,
                        true
                    )
                )

                binding.group.visibility = View.INVISIBLE
                binding.loading.visibility = View.VISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    dismiss()
                }, 1500)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}