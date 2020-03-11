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

    //Функция обнуляет строку с выражением и результатом
    fun clearAll(view: View){
        InfixNotation_txtEdit.setText("")
        Result_txt.text = ""
    }

    //Функция обрабатывается при нажатии кнопки Результат. Считывает выражение, и выводит результат
    fun doResult(view: View){
        var expression: String = InfixNotation_txtEdit.text.toString()      //Из поля ввода в активити перекладываем значение в строку
        expression = expression.replace(" ", "")              //удаление пробелов из выражения

        Result_txt.text = doExpression(expression)
    }

    //Функция ищет операнды в строке, возвращает массив символов операндов
    private fun parseOperands(Expression: String): Array <String> {
        var indexOperand: Int = 0              //индекс массива операндов
        var countOperand : Int = 0
        Expression.forEach {el ->
            if ((el == '+') || (el == '-') || (el == '*') || (el == '/')) {     //Если в строке выражения есть операнд,
                countOperand++
            }
        }
        var operandsArray = Array<String>(countOperand) {""}     //объявляем массив операндов
        Expression.forEach {el ->       //Если перебор всей строки, и поиск операндов
            if ((el == '+') || (el == '-') || (el == '*') || (el == '/')) {     //Если в строке выражения есть операнд,
                operandsArray[indexOperand] = el.toString()   //то добавить операнд в массив операндов
                indexOperand++
            }
        }
        return operandsArray
    }

    //Функция ищет операнды, и возвращает массив индексов порядка выполнения операндов
    private fun orderOfOperands(operands: Array<String>): Array<Int>{
        var order = Array<Int>(operands.size) {0}   //массив порядка операндов
        var indexOrder: Int = 0       //инкримирующий порядок
        operands.forEachIndexed {index, el ->
            if (el.toString() == "*" || el.toString() == "/") {         //если операндом является умножение или деление,
                order[indexOrder] = index     //то в массиве порядка указать индекс операнда в массиве операндов
                indexOrder++                  //инкримировать счётчик индекса
            }
        }
        operands.forEachIndexed {index, el ->
            if (el.toString() == "+" || el.toString() == "-") {         //если операндом является сложение или вычитание,
                order[indexOrder] = index     //то в массиве порядка указать индекс операнда в массиве операндов
                indexOrder++                  //инкримировать счётчик индекса
            }
        }
        return order
    }

    //Функция возвращает массив индексов операндов в выражении
    private fun parseOperandIndexFromExpressionArray(Expression: String, operands: Array<String>): Array<Int>{
        var indexOperand: Int = 0
        var operandIndexArray = Array<Int>(operands.size) {0}
        Expression.forEachIndexed {index, el ->
            if ((el == '+') || (el == '-') || (el == '*') || (el == '/')) {     //Если в строке выражения есть операнд,
                operandIndexArray[indexOperand] =  index   //то добавить индекс операнда в массив операндов
                indexOperand++
            }
        }
        return operandIndexArray
    }

    //Функция разбивает выражение на переменные, возвращая массив. Для того, чтобы с элементами массива выполнять операции
    private fun parseVariablesArray(expression: String, operandsIndex: Array<Int>): Array <Int>{
        var variablesStr = Array<String>(expression.length - operandsIndex.size) {""}   //массив переменных, каждая переменная - строка
        var variablesInt = Array<Int>(expression.length - operandsIndex.size) {0}        //массив переменных, каждая переменная - число
        var indexOperands : Int = 0     //индекс массива индексов операндов
        var indexVar: Int = 0           //индекс массива переменных
        expression.forEachIndexed {index, el ->     //перебор строки с выражением
            if (index < operandsIndex[indexOperands] && indexOperands <= operandsIndex.lastIndex){      //если индекс текущего элемента меньше индекса операнда, и не вышли за перделы массива операндов
                variablesStr[indexVar] += el.toString()      //то переложить его в отдельный массив с элементами
            }
            if (index == operandsIndex[indexOperands] && indexOperands <= operandsIndex.lastIndex)        //если мы попали на индекс операнда,
            {
                indexVar++          //то инкримировать индекс массив переменных
                if (indexOperands < operandsIndex.lastIndex) {      //инкримировать индекс массива индексов операндов, если выбран не последний
                    indexOperands++     //и инкримировать массив операндов
                }
            }
            if (index > operandsIndex[operandsIndex.lastIndex]){      //если индекс текущего элемента больше индекса последнего операнда,
                variablesStr[indexVar] += el.toString()      //то переложить его в отдельный массив с элементами
            }
        }
        variablesStr.forEachIndexed{index, el ->        //перевод всех элементов массива в целочисленный формат
            variablesInt[index] = el.toInt()
        }
        return variablesInt
    }

    //функция удаления элемента из массива (обнуления и смещения)
    private fun removeItemArrayInt(inputArray:Array <Int>, indexRemove: Int): Array<Int>{
        var resultArray = Array<Int>(inputArray.size - 1) {0}
        resultArray.forEachIndexed{index, el ->
            if (index >= indexRemove) {
                resultArray[index] = inputArray[index + 1]
            }
            else {
                resultArray[index] = inputArray[index]
            }
        }
        return resultArray
    }
    private fun removeItemArrayString(inputArray:Array <String>, indexRemove: Int): Array<String>{
        var resultArray = Array<String>(inputArray.size - 1) {""}
        resultArray.forEachIndexed{index, el ->
            if (index >= indexRemove) {
                resultArray[index] = inputArray[index + 1]
            }
            else {
                resultArray[index] = inputArray[index]
            }
        }
        return resultArray
    }

    //Функция делает вычисления, исходя из приоритетов операций, и перебирая массивы элементов
    private fun calculation(variables: Array<Int>, operands: Array<String>, orderOperands: Array<Int>): String{
        var resultStr : String = ""
        var tmpVarArray = variables
        var tmpOrderOperands = orderOperands
        var tmpOperands = operands
        var indexVar: Int = 0
        for (indexOrder in 0 .. (tmpOrderOperands.size - 1)){       //Перебор в массиве очереди операций
                indexVar = tmpOrderOperands[indexOrder]     //текущая переменная в том же порядке, что и операция, которая сейчас в очереди
                tmpVarArray[indexVar] = operationVariables(
                    tmpVarArray[indexVar],
                    tmpVarArray[indexVar + 1],
                    tmpOperands[tmpOrderOperands[indexOrder]]
                )     //в нужный элемент массива присваивается вычисленное значение, в соответствии с операндом
                tmpVarArray = removeItemArrayInt(
                    tmpVarArray,
                    indexVar + 1
                )     //удаление следующего элемента из массива со смещением
                tmpOperands = removeItemArrayString(
                    tmpOperands,
                    tmpOrderOperands[indexOrder]
                )      //удаление операции из упорядоченного массива очереди операций со смещением
            tmpOrderOperands.forEachIndexed{index, el ->    //уменьшение порядка после прохождения списка
                tmpOrderOperands[index]--
            }
        }
        resultStr = tmpVarArray[0].toString()
        return resultStr
    }

    //Функция принимает на вход строку выражения калькулятора, и парсит её на переменные и операнды для выполнения отдельных операций
    private fun doExpression(Expression: String): String {
        var result: String = ""                     //строковая переменная с результатом

        var operandsArray: Array<String> = parseOperands(Expression)      //вызов функции назначения массивов операндов

        var indexOperandsArray: Array<Int> = parseOperandIndexFromExpressionArray(Expression, operandsArray)  //вызов функции назначения массива индексов операндов

        var orderOperandsArray : Array<Int> = orderOfOperands(operandsArray)   //вызов функции назначения массива индексов порядка операндов

        var variableArray : Array<Int> = parseVariablesArray(Expression, indexOperandsArray)           //массив переменных

        result = calculation(variableArray, operandsArray, orderOperandsArray)

        return result
    }

    //Функция на вход принимает два агрумента и операнд, затем производит операцию
    private fun operationVariables(varA: Int, varB: Int, Operand: String): Int {
        var result: Int = 0        //переменная будет выводить результат
        result = when (Operand) {     //оператор выбора. в зависимости от используемого операнда, будет вычислен результат
            "+" -> varA + varB       //если сложение, то выполнить операцию сложения
            "-" -> varA - varB       //если вычитание, то выполнить вычитание
            "*" -> varA * varB       //если умножение, то выполнить умножение
            "/" -> (varA / varB) - (varA % varB)       //если деление, то вернуть целочисленный остаток от деления
            else -> 0
        }
        return result
    }

}


