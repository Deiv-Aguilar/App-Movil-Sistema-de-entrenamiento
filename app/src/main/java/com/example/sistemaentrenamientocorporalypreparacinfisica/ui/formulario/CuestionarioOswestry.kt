package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.formulario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sistemaentrenamientocorporalypreparacinfisica.R

class CuestionarioOswestry : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cuestionario_oswestry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mapeo de los RadioGroups mediante findViewById
        val radioGroups = listOf(
            view.findViewById<RadioGroup>(R.id.rgSeccion1),
            view.findViewById<RadioGroup>(R.id.rgSeccion2),
            view.findViewById<RadioGroup>(R.id.rgSeccion3),
            view.findViewById<RadioGroup>(R.id.rgSeccion4),
            view.findViewById<RadioGroup>(R.id.rgSeccion5),
            view.findViewById<RadioGroup>(R.id.rgSeccion6),
            view.findViewById<RadioGroup>(R.id.rgSeccion7),
            view.findViewById<RadioGroup>(R.id.rgSeccion8),
            view.findViewById<RadioGroup>(R.id.rgSeccion9),
            view.findViewById<RadioGroup>(R.id.rgSeccion10)
        )

        val btnCalcular = view.findViewById<Button>(R.id.btnCalcularOswestry)
        val tvResultado = view.findViewById<TextView>(R.id.tvResultadoOswestry)
        val scrollView = view.findViewById<ScrollView>(R.id.scrollViewOswestry)

        btnCalcular.setOnClickListener {
            var puntosTotales = 0
            var preguntasRespondidas = 0

            for (rg in radioGroups) {
                val selectedId = rg.checkedRadioButtonId
                if (selectedId != -1) {
                    val radioButton = rg.findViewById<RadioButton>(selectedId)
                    val puntosOpcion = rg.indexOfChild(radioButton)
                    puntosTotales += puntosOpcion
                    preguntasRespondidas++
                }
            }

            if (preguntasRespondidas < radioGroups.size) {
                Toast.makeText(
                    requireContext(),
                    "Por favor responde todas las secciones antes de calcular.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val porcentajeIncapacidad = (puntosTotales.toDouble() / (radioGroups.size * 5)) * 100

            val nivelDiscapacidad = when {
                porcentajeIncapacidad <= 20 -> "Discapacidad mínima (0 - 20%)"
                porcentajeIncapacidad <= 40 -> "Discapacidad moderada (21 - 40%)"
                porcentajeIncapacidad <= 60 -> "Discapacidad severa (41 - 60%)"
                porcentajeIncapacidad <= 80 -> "Incapaz / Discapacidad grave (61 - 80%)"
                else -> "Postrado en cama / Exageración de síntomas (81 - 100%)"
            }

            val mensaje = "Puntaje: $puntosTotales/50\nPorcentaje: ${String.format("%.1f", porcentajeIncapacidad)}%\n$nivelDiscapacidad"

            tvResultado.text = mensaje
            tvResultado.visibility = View.VISIBLE

            // Hace que la pantalla baje solita y suavemente hasta el resultado
            scrollView.post {
                scrollView.smoothScrollTo(0, tvResultado.bottom)
            }
        }
    }
}