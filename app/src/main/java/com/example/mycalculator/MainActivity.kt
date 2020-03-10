package com.example.mycalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun incNumberFromString(view: View){
        var numberStr: String = InfixNotation_txtEdit.text.toString()
        var numb: Int = numberStr.toInt()
        var res: Int = numb + numb
        Result_txt.text = res.toString()
    }

    fun 

}
