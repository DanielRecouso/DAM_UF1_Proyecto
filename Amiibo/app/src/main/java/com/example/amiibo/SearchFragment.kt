package com.example.amiibo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amiibo.databinding.FragmentSearchBinding
import com.example.amiibo.recyclerview.AmiiboAdapter
import com.example.amiibo.viewmodel.AmiiboViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    var _binding: FragmentSearchBinding? = null
    val binding: FragmentSearchBinding
        get() = _binding!!
    private lateinit var amiiboViewModel: AmiiboViewModel
    private lateinit var amiiboAdapter: AmiiboAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val view = binding.root
        val searchAmiibo = binding.searchAmiibo
        val recyclerView = binding.amiiboRecyclerView
        var filter = true
        var currentText = ""

//        Inicializamos recyclerView, e viewModel y el adaptador
        recyclerView.layoutManager = LinearLayoutManager(context)
        amiiboViewModel = ViewModelProvider(this)[AmiiboViewModel::class.java]
        amiiboAdapter = AmiiboAdapter(emptyList(), emptyList(), amiiboViewModel) { updatedSelectedList ->
            amiiboViewModel.saveSelectedAmiibos(updatedSelectedList)
        }
        recyclerView.adapter = amiiboAdapter

//      Filtramos cada vez que se pulsa un radioButton y cambiamos la variable filter para el onQueryTextChange

        binding.categoryRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                // Si el radioButton character está seleccionado
                binding.radioCharacter.id -> {
                    filter = true
                    amiiboViewModel.fetchAmiibosFromCharacter(currentText)
                }
                // Si el radioButton game está seleccionado
                binding.radioGame.id -> {
                    filter = false
                    amiiboViewModel.fetchAmiibosFromGame(currentText)
                }
            }
        }

//      Expandimos el search al pulsar si no lo está y desplegamos el teclado
        searchAmiibo.setOnClickListener {
            searchAmiibo.isIconified = false
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(searchAmiibo, InputMethodManager.SHOW_IMPLICIT)
        }

//        Pongo un listener para que la recyclerView se actualice a la vez que escribimos
        searchAmiibo.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

//          Esta variable sirve para poner un delay cuando se hace la búsqueda
            private var searchJob: Job? = null

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(300) // espera 300ms antes de ejecutar para que le de tiempo a procesar
                    currentText = newText.orEmpty()
//                  Filtramos
                    if (filter) {
                        amiiboViewModel.fetchAmiibosFromCharacter(currentText)
                    } else {
                        amiiboViewModel.fetchAmiibosFromGame(currentText)
                    }
                }
                return true
            }
        })

        // Updateamos los favoritos
        amiiboViewModel.selectedAmiibos.observe(viewLifecycleOwner) { favorites ->
            amiiboAdapter.updateFavourites(favorites)
        }

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