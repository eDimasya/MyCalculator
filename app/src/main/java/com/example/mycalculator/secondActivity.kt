package com.example.mycalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class secondActivity : AppCompatActivity() {

    companion object{
        const val TOTAL_COUNT = "total_count"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //showRandomNumber()
    }

    /*fun showRandomNumber(){
        val count = intent.getIntExtra(TOTAL_COUNT, 0)
        val random = Random()
        var randomInt = 0
        if (count > 0) {
            randomInt = random.nextInt(count + 1)
        }
        textView_RandomValue.text = Integer.toString(randomInt)
        textView_Label.text = getString(R.string.random_heading, count)
    }*/
}
