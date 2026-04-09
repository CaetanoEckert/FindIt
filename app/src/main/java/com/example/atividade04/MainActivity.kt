package com.example.atividade04

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Lista com as imagens das faces das cartas
    private var faces = mutableListOf(R.drawable.img_copas, R.drawable.img_espada, R.drawable.img_copas)
    private var jogoAtivo = true // Para impedir múltiplos cliques após a escolha

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val carta1 = findViewById<ImageView>(R.id.imV_carta1)
        val carta2 = findViewById<ImageView>(R.id.imV_carta2)
        val carta3 = findViewById<ImageView>(R.id.imV_carta3)
        val tvResultado = findViewById<TextView>(R.id.tv_resultado)
        val btnEmbaralhar = findViewById<Button>(R.id.bt_embaralhar)

        // Começa o app já sorteando e configurando
        embaralhar(carta1, carta2, carta3, tvResultado)

        // Configura os cliques
        val cartas = listOf(carta1, carta2, carta3)
        cartas.forEachIndexed { index, iv ->
            iv.setOnClickListener {
                if (jogoAtivo) {
                    revelarEscolha(iv, faces[index], cartas, tvResultado)
                }
            }
        }

        btnEmbaralhar.setOnClickListener {
            embaralhar(carta1, carta2, carta3, tvResultado)
        }

    }

    private fun revelarEscolha(clicada: ImageView, face: Int, todas: List<ImageView>, tv: TextView) {
        jogoAtivo = false // Trava o jogo
        clicada.setImageResource(face) // Mostra a carta clicada imediatamente

        // Espera 2 segundos (2000 milisegundos)
        Handler(Looper.getMainLooper()).postDelayed({
            // 1. Revela todas as outras
            todas.forEachIndexed { i, iv -> iv.setImageResource(faces[i]) }

            // 2. Verifica se ganhou (se a face clicada era a de espada)
            if (face == R.drawable.img_espada) {
                tv.text = "VOCÊ GANHOU! 🎉"
                tv.setTextColor(android.graphics.Color.GREEN)
            } else {
                tv.text = "VOCÊ PERDEU! ❌"
                tv.setTextColor(android.graphics.Color.RED)
            }
        }, 2000)
    }

    private fun embaralhar(c1: ImageView, c2: ImageView, c3: ImageView, tv: TextView) {
        jogoAtivo = true
        tv.text = "" // Limpa o resultado
        faces.shuffle() // Sorteia as posições

        val cartas = listOf(c1, c2, c3)
        cartas.forEach {
            it.setImageResource(R.drawable.img_verso) // Vira para baixo
            it.tag = "fechada"
        }
    }
}