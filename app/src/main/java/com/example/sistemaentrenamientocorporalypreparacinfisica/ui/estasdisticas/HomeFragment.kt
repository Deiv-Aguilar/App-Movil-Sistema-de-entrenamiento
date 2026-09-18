package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EstadisticasViewModel
    var idUserV: String? = null
    var loginSucess: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.estadisticasButon.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToEstadisticasEjercicio(
                    idUser = idUserV!!.toInt()
                )
            )
        }

        binding.upData.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToUpdateDatos(
                    idUser = idUserV!!.toInt()
                )
            )
        }

        // Clic del nuevo botón para la evaluación clínica
        binding.btnRealizarEvaluacion.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToSeleccionEvaluacion()
            findNavController().navigate(action)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        // En caso de no haber iniciado sesión retorna a la pantalla de registro
        lifecycleScope.launch {
            loginSucess = MainActivity.getLoginSucces(requireContext())
            if (!loginSucess!!) {
                findNavController().navigate(R.id.firts_login)
                mainActivity.hideBottomNav()
            } else {
                idUserV = MainActivity.getIdUser(requireContext())
                // Solo ejecutar este código cuando dataSesion no sea null
                idUserV?.let {
                    val factory = EstadisticasViewModelFactory(it, requireContext())
                    viewModel = ViewModelProvider(this@HomeFragment, factory).get(EstadisticasViewModel::class.java)
                    observeViewModel()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.usuarioAvancesData.observe(viewLifecycleOwner, Observer { usuario ->
            usuario?.let { it ->
                binding.altura.text = it.altura.toString()
                binding.peso.text = it.peso.toString()

                val bmi = it.peso.toDouble() / Math.pow(it.altura, 2.0)
                binding.bmi.text = String.format("%.3f", bmi)

                val kilosG = it.peso * ((it.pesoGrasa ?: 0.0) / 100.0)
                val kilosM = it.peso * ((it.pesoMusculo ?: 0.0) / 100.0)

                binding.pesoGrasaPorcentaje.text = String.format("%.2f", it.pesoGrasa ?: 0.0)
                binding.pesoDeGrasaKilos.text = String.format("%.2f", kilosG)

                binding.pesoMusculosPorcentaje.text = String.format("%.2f", it.pesoMusculo ?: 0.0)
                binding.pesoDeMusculosKilos.text = String.format("%.2f", kilosM)

                if (bmi < 18.5) {
                    binding.estatusGeneral.text = "Peso inferior al normal"
                    binding.progressBarEstado.progress = 12
                } else if (bmi >= 18.5 && bmi < 24.9) {
                    binding.estatusGeneral.text = "Normal"
                    binding.progressBarEstado.progress = 37
                } else if (bmi >= 25.0 && bmi < 29.9) {
                    binding.estatusGeneral.text = "Peso superior al normal"
                    binding.progressBarEstado.progress = 62
                } else {
                    binding.estatusGeneral.text = "Obesidad"
                    binding.progressBarEstado.progress = 87
                }
            } ?: run {
                binding.altura.text = "-"
                binding.peso.text = "-"
                binding.bmi.text = "-"
                binding.pesoGrasaPorcentaje.text = "-"
                binding.pesoDeGrasaKilos.text = "-"
                binding.pesoMusculosPorcentaje.text = "-"
                binding.pesoDeMusculosKilos.text = "-"
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized) {
            viewModel.updateData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}