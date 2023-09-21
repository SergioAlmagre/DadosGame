package com.example.dadosgame

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.dadosgame.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var allDices = arrayListOf<String>("dado1","dado2","dado3","dado4","dado5","dado6")
    var player1Points = 0
    var player2Points = 0
    var turn = 0
    var auto = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Coloca una imagen
        val resourceId = resources.getIdentifier("dados", "drawable", packageName)
        binding.imageView.setImageResource(resourceId)

        binding.buttonPlayer1.setBackgroundColor((Color.parseColor("#CEFFCC")))

        binding.buttonThrowDice.setOnClickListener{
            if (!someoneWin()){
                throwDice(this)
            }
        }


        binding.buttonExit.setOnClickListener{
            finish()
        }

        binding.buttonRestartGame.setOnClickListener{
            restart()
        }
    }

    fun throwDice(context: Context){

        auto = binding.autoSwitchID.isChecked

        CoroutineScope(Dispatchers.Main).launch {
            var randomDice = ""
            for (i in 0..20) {
                randomDice = allDices[Random.nextInt(0,allDices.size-1)]
                val resourceId = resources.getIdentifier(randomDice, "drawable", packageName)
                binding.imageView.setImageResource(resourceId)
                // Pausar durante 50 milisegundos
                delay(50)
            }
            var index = 1
            var diceValue = 0
            for (d in allDices){
                if (d == randomDice){
                    diceValue = index
                }
                index++
            }
            if (turn == 0){
                binding.buttonPlayer1.setBackgroundColor((Color.parseColor("#EFEFEF")))
                binding.buttonPlayer2.setBackgroundColor((Color.parseColor("#CEFFCC")))

                player1Points = player1Points + diceValue
                binding.seekBarP1.progress = player1Points
                turn = 1
                if (auto && turn == 1){
                    delay(1000)
                    throwDice(context)
                }
            }else{
                binding.buttonPlayer1.setBackgroundColor((Color.parseColor("#CEFFCC")))
                binding.buttonPlayer2.setBackgroundColor((Color.parseColor("#EFEFEF")))
                player2Points = player2Points + diceValue
                binding.seekBarP2.progress = player2Points
                turn = 0
            }
            binding.nPointsP1.text = player1Points.toString()
            binding.nPointsP2.text = player2Points.toString()

            var winner = 1
            if (someoneWin()){
                if (player1Points<player2Points){
                    winner = 2
                }
                Toast.makeText(context,"Player ${winner} is the winner",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun restart(){
        binding.buttonPlayer1.setBackgroundColor((Color.parseColor("#CEFFCC")))
        player1Points = 0
        player2Points = 0
        binding.nPointsP1.text = player1Points.toString()
        binding.nPointsP2.text = player2Points.toString()
        val resourceId = resources.getIdentifier("dados", "drawable", packageName)
        binding.imageView.setImageResource(resourceId)
        binding.seekBarP1.progress = player1Points
        binding.seekBarP2.progress = player2Points
    }

    fun someoneWin():Boolean{
        var win = false
        if (player1Points >= 20 || player2Points >= 20){
            win = true
        }
        return win
    }

}