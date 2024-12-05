package com.example.amiibo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amiibo.databinding.FragmentFavsBinding
import com.example.amiibo.recyclerview.AmiiboAdapter
import com.example.amiibo.viewmodel.AmiiboViewModel

class FavsFragment : Fragment() {

    var _binding: FragmentFavsBinding? = null
    val binding: FragmentFavsBinding
        get() = _binding!!
    private lateinit var amiiboViewModel: AmiiboViewModel
    private lateinit var amiiboAdapter: AmiiboAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavsBinding.inflate(inflater, container, false)
        val view = binding.root

        amiiboViewModel = ViewModelProvider(this)[AmiiboViewModel::class.java]

        // Configurar RecyclerView
        val recyclerView = binding.amiiboRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)



        amiiboViewModel.selectedAmiibos.observe(viewLifecycleOwner) { favorites ->
            amiiboAdapter.updateFavourites(favorites)
        }
        amiiboViewModel.amiibos.observe(viewLifecycleOwner) { amiibos ->
            val selectedAmiibos = amiiboViewModel.getSelectedAmiibos()
            val filteredAmiibos = amiibos.filter { it in selectedAmiibos }
            amiiboAdapter = AmiiboAdapter(filteredAmiibos,selectedAmiibos,amiiboViewModel) { updatedSelectedList ->
                amiiboViewModel.saveSelectedAmiibos(updatedSelectedList)
            }
            recyclerView.adapter = amiiboAdapter
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}