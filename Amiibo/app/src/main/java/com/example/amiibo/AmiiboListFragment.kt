package com.example.amiibo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amiibo.databinding.FragmentAmiiboListBinding
import com.example.amiibo.recyclerview.AmiiboAdapter
import com.example.amiibo.viewmodel.AmiiboViewModel

class AmiiboListFragment : Fragment() {


    var _binding: FragmentAmiiboListBinding? = null
    val binding: FragmentAmiiboListBinding
        get() = _binding!!
    private lateinit var amiiboViewModel: AmiiboViewModel
    private lateinit var amiiboAdapter: AmiiboAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAmiiboListBinding.inflate(inflater, container, false)
        val view = binding.root
        amiiboViewModel = ViewModelProvider(this)[AmiiboViewModel::class.java]

        // Configurar RecyclerView
        val recyclerView = binding.amiiboRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)


        // Updateamos los favoritos
        amiiboViewModel.selectedAmiibos.observe(viewLifecycleOwner) { favorites ->
            amiiboAdapter.updateFavourites(favorites)
        }

        // Observe the amiibos and selected list
        amiiboViewModel.amiibos.observe(viewLifecycleOwner) { amiibos ->
            val selectedAmiibos = amiiboViewModel.getSelectedAmiibos() // Recuperar favoritos guardados
            amiiboAdapter = AmiiboAdapter(amiibos, selectedAmiibos, amiiboViewModel) { updatedSelectedList ->
                amiiboViewModel.saveSelectedAmiibos(updatedSelectedList) // Guardar cambios
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