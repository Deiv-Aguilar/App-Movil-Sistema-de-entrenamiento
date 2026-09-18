package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sistemaentrenamientocorporalypreparacinfisica.HealthConnectManager
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentUpdateDatosBinding
import kotlinx.coroutines.launch
import java.util.Locale

class UpdateDatos : Fragment() {
    private var _binding: FragmentUpdateDatosBinding? = null
    private val binding get() = _binding!!
    private val args: UpdateDatosArgs by navArgs()
    private lateinit var viewModel: UpdateDatosViewModel
    private lateinit var estadisticasViewModel: EstadisticasViewModel
    private lateinit var healthConnectManager: HealthConnectManager

    private val permisosHealth = setOf(
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(HeightRecord::class),
        HealthPermission.getReadPermission(BodyFatRecord::class),
        HealthPermission.getReadPermission(LeanBodyMassRecord::class)
    )

    private lateinit var requestPermissions: ActivityResultLauncher<Set<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionContract = PermissionController.createRequestPermissionResultContract()
        requestPermissions = registerForActivityResult(requestPermissionContract) { granted ->
            if (granted.containsAll(permisosHealth)) {
                ejecutarSincronizacionYGuardado()
            } else {
                Toast.makeText(context, "Se requieren permisos para sincronizar con la báscula.", Toast.LENGTH_SHORT).show()
            }
        }

        val factory = UpdateDatosViewModelFactory(args.idUser.toString(), requireContext())
        viewModel = ViewModelProvider(this, factory).get(UpdateDatosViewModel::class.java)

        estadisticasViewModel = ViewModelProvider(
            requireActivity(),
            EstadisticasViewModelFactory(args.idUser.toString(), requireContext())
        ).get(EstadisticasViewModel::class.java)

        healthConnectManager = HealthConnectManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateDatosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Limpia peso, grasa y músculo al abrir
        binding.entradaPeso.setText("")
        binding.entradagrasa.setText("")
        binding.entradaMusculo.setText("")

        // Carga la última altura guardada para facilitar la captura al usuario
        viewModel.usuarioAvances.observe(viewLifecycleOwner) { avances ->
            avances?.altura?.let { alturaGuardada ->
                if (binding.entradaAltura.text.isNullOrEmpty() && alturaGuardada > 0) {
                    binding.entradaAltura.setText(String.format(Locale.US, "%.2f", alturaGuardada))
                }
            }
        }

        // Listener del botón único
        binding.btnSincronizarYGuardar.setOnClickListener {
            ejecutarSincronizacionYGuardado()
        }

        viewModel.datosActualizados.observe(viewLifecycleOwner) { actualizado ->
            if (actualizado) {
                estadisticasViewModel.updateData()
            }
        }
    }

    private fun ejecutarSincronizacionYGuardado() {
        val alturaTexto = binding.entradaAltura.text.toString()

        // 1. Validar que la estatura manual esté ingresada correctamente
        if (!validarNumero(alturaTexto) || alturaTexto.toDouble() <= 0) {
            Toast.makeText(context, "Por favor, ingresa tu estatura antes de continuar.", Toast.LENGTH_LONG).show()
            binding.entradaAltura.requestFocus()
            return
        }

        lifecycleScope.launch {
            // 2. Verificar disponibilidad y permisos de Health Connect
            if (!healthConnectManager.isAvailable()) {
                Toast.makeText(context, "Health Connect no está disponible en este dispositivo.", Toast.LENGTH_LONG).show()
                return@launch
            }

            val tienePermisos = healthConnectManager.hasAllPermissions(permisosHealth)
            if (!tienePermisos) {
                requestPermissions.launch(permisosHealth)
                return@launch
            }

            // 3. Traer datos desde Health Connect
            val peso = healthConnectManager.getUltimoPeso()
            val grasa = healthConnectManager.getUltimaGrasaCorporal()
            val musculoKg = healthConnectManager.getUltimaMasaMuscular()

            if (peso == null && grasa == null && musculoKg == null) {
                Toast.makeText(context, "No se encontraron lecturas recientes de la báscula.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // 4. Calcular el porcentaje de músculo
            val porcentajeMusculo = if (musculoKg != null && peso != null && peso > 0) {
                if (musculoKg < peso) {
                    (musculoKg / peso) * 100.0
                } else {
                    musculoKg
                }
            } else {
                null
            }

            // Reflejar en interfaz los valores obtenidos
            peso?.let { binding.entradaPeso.setText(String.format(Locale.US, "%.1f", it)) }
            grasa?.let { binding.entradagrasa.setText(String.format(Locale.US, "%.1f", it)) }
            porcentajeMusculo?.let { binding.entradaMusculo.setText(String.format(Locale.US, "%.1f", it)) }

            // 5. Guardar todo directamente en la base de datos
            val usuario = viewModel.usuarioAvances.value
            val idAvances = usuario?.idUserAvances

            viewModel.insertDataUsuariosAvances(
                idUsuarioAvances = idAvances,
                peso = peso ?: 0.0,
                altura = alturaTexto.toDouble(),
                pesoGrasa = grasa,
                pesoMusculo = porcentajeMusculo
            ) { result ->
                if (result) {
                    Toast.makeText(context, "¡Datos sincronizados y guardados correctamente!", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(context, "Fallo al guardar los datos.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validarNumero(numero: String): Boolean {
        return numero.toDoubleOrNull() != null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}