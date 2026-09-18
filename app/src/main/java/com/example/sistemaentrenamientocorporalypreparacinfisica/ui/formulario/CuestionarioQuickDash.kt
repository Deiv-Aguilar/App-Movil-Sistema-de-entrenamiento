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

class CuestionarioQuickDash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cuestionario_quickdash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            view.findViewById<RadioGroup>(R.id.rgSeccion10),
            view.findViewById<RadioGroup>(R.id.rgSeccion11)
        )

        val btnCalcular = view.findViewById<Button>(R.id.btnCalcularQuickDash)
        val tvResultado = view.findViewById<TextView>(R.id.tvResultadoQuickDash)
        val scrollView = view.findViewById<ScrollView>(R.id.scrollViewQuickDash)

        btnCalcular.setOnClickListener {
            var sumaPuntajes = 0
            var preguntasRespondidas = 0

            for (rg in radioGroups) {
                val selectedId = rg.checkedRadioButtonId
                if (selectedId != -1) {
                    val radioButton = rg.findViewById<RadioButton>(selectedId)
                    val puntosOpcion = rg.indexOfChild(radioButton) + 1 // Escala del 1 al 5
                    sumaPuntajes += puntosOpcion
                    preguntasRespondidas++
                }
            }

            // Regla oficial QuickDASH: Se requiere responder al menos 10 de las 11 preguntas
            if (preguntasRespondidas < 10) {
                Toast.makeText(
                    requireContext(),
                    "Por favor responde al menos 10 de las 11 secciones antes de calcular.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Fórmula oficial: [(Suma de respuestas / n) - 1] * 25
            val puntajeQuickDash = ((sumaPuntajes.toDouble() / preguntasRespondidas) - 1.0) * 25.0

            val mensaje = "Puntaje QuickDASH: ${String.format("%.1f", puntajeQuickDash)} / 100\n(Respondidas: $preguntasRespondidas/11)"

            tvResultado.text = mensaje
            tvResultado.visibility = View.VISIBLE

            // Desplazamiento automático hacia abajo suavemente
            scrollView.post {
                scrollView.smoothScrollTo(0, tvResultado.bottom)
            }
        }
    }
}