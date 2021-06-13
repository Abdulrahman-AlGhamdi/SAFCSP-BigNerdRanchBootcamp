package com.ss.restaloca.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ss.restaloca.R
import com.ss.restaloca.database.RestaLocaDatabase
import com.ss.restaloca.databinding.FragmentReservationBinding

class ReservationFragment : Fragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var adapter: ReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)

        // Initialize views and variables
        init()
        // Get reservations from the database
        getAllReservation()
        // delete reservation from the database
        deleteReservation()

        return binding.root
    }

    private fun init() {
        // Initialize instance of reservation adapter
        adapter = ReservationAdapter()

        // Initialize instance of reservation view model
        val repository = ReservationRepository(RestaLocaDatabase(requireContext()))
        val factory = ReservationProviderFactory(repository)
        reservationViewModel = ViewModelProvider(this, factory)[ReservationViewModel::class.java]

        // Bottom navigation configuration
        binding.bottomNavigation.background = null
        binding.bottomNavigation.menu.getItem(1).isEnabled = false
        binding.bottomNavigation.menu.getItem(2).isEnabled = false
        binding.bottomNavigation.setupWithNavController(findNavController())

        // Fab button click handle
        binding.fabNavigation.setOnClickListener {
            val action = ReservationFragmentDirections.actionReservationFragmentToMapsFragment()
            findNavController().navigate(action)
        }
    }

    private fun getAllReservation() {
        reservationViewModel.getAllReservation().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.reservationRecycler.visibility = View.INVISIBLE
                binding.emptyReservation.visibility = View.VISIBLE
            } else {
                binding.reservationRecycler.layoutManager = LinearLayoutManager(requireContext())
                adapter.differ.submitList(it)
                binding.reservationRecycler.adapter = adapter
                binding.emptyReservation.visibility = View.INVISIBLE
                binding.reservationRecycler.visibility = View.VISIBLE
            }
        })
    }

    private fun deleteReservation() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val reservation = adapter.differ.currentList[position]
                reservationViewModel.deleteReservation(reservation)
                reservation.reservationAdded = false
                Snackbar.make(
                    requireView(),
                    R.string.successfully_deleted,
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(R.string.undo) {
                        reservationViewModel.addReservation(reservation)
                        reservation.reservationAdded = true
                    }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.reservationRecycler)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}