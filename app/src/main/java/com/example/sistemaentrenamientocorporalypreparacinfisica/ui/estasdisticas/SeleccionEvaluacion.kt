package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentSeleccionEvaluacionBinding

class SeleccionEvaluacion : Fragment() {

    private var _binding: FragmentSeleccionEvaluacionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeleccionEvaluacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Clic en Oswestry
        binding.cardOswestry.setOnClickListener {
            val action = SeleccionEvaluacionDirections.actionSeleccionEvaluacionToCuestionarioOswestry()
            findNavController().navigate(action)
        }

        // Clic en QuickDASH (¡Ya con su acción vinculada!)
        binding.cardQuickDash.setOnClickListener {
            val action = SeleccionEvaluacionDirections.actionSeleccionEvaluacionToCuestionarioQuickDash()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}