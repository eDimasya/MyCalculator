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
        if (checkExpression(expression)) {          //Если проверка пройдена
            expression = expression.replace(" ", "")              //удаление пробелов из выражения
            Result_txt.text = doExpression(expression)      //то вычислять
        }
        else
        {
            Result_txt.text = "Error Expression!!"      //иначе, вывести ошибку
        }
    }

    //Проверка правильности ввода
    private fun checkExpression(Expression: String): Boolean{
        var checkResult: Boolean = true         //переменная, отвечающая за проверку



        return checkResult
    }

    //Функция ищет операнды в строке, возвращает массив символов операндов
    private fun parseOperands(Expression: String): Array <String> {
        var indexOperand: Int = 0              //индекс массива операндов
        var countOperand : Int = 0
        Expression.forEach {el ->               //посчитать количество операндов для создания массива
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
    private fun orderOfOperands(Operands: Array<String>): Array<Int>{
        var order = Array<Int>(Operands.size) {0}   //массив порядка операндов
        var indexOrder: Int = 0       //инкримирующий порядок
        Operands.forEachIndexed { index, el ->
            if (el.toString() == "*" || el.toString() == "/") {         //если операндом является умножение или деление,
                order[indexOrder] = index     //то в массиве порядка указать индекс операнда в массиве операндов
                indexOrder++                  //инкримировать счётчик индекса
            }
        }
        Operands.forEachIndexed { index, el ->
            if (el.toString() == "+" || el.toString() == "-") {         //если операндом является сложение или вычитание,
                order[indexOrder] = index     //то в массиве порядка указать индекс операнда в массиве операндов
                indexOrder++                  //инкримировать счётчик индекса
            }
        }
        return order
    }

    //Функция возвращает массив индексов операндов в выражении
    private fun parseOperandIndexFromExpressionArray(Expression: String, Operands: Array<String>): Array<Int>{
        var indexOperand: Int = 0
        var operandIndexArray = Array<Int>(Operands.size) {0}
        Expression.forEachIndexed {index, el ->
            if ((el == '+') || (el == '-') || (el == '*') || (el == '/')) {     //Если в строке выражения есть операнд,
                operandIndexArray[indexOperand] =  index   //то добавить индекс операнда в массив операндов
                indexOperand++
            }
        }
        return operandIndexArray
    }

    //Функция разбивает выражение на переменные, возвращая массив. Для того, чтобы с элементами массива выполнять операции
    private fun parseVariablesArray(Expression: String, OperandsIndex: Array<Int>): Array <Int>{
        var variablesStr = Array<String>(OperandsIndex.size + 1) {""}   //массив переменных, каждая переменная - строка
        var variablesInt = Array<Int>(OperandsIndex.size + 1) {0}        //массив переменных, каждая переменная - число
        var indexOperands : Int = 0     //индекс массива индексов операндов
        var indexVar: Int = 0           //индекс массива переменных
        Expression.forEachIndexed { index, el ->     //перебор строки с выражением
            if (index != OperandsIndex[indexOperands]){      //если индекс текущего элемента меньше индекса операнда, и не вышли за перделы массива операндов
                variablesStr[indexVar] += el.toString()      //то переложить его в отдельный массив с элементами
            }
            if (index == OperandsIndex[indexOperands])        //если мы попали на индекс операнда,
            {
                indexVar++          //то инкримировать индекс массив переменных
                if (indexOperands < OperandsIndex.lastIndex) {      //во избежание переполнения проверяем, что выбран не последний операнд
                    indexOperands++     //и инкримировать массив операндов
                }
            }
        }
        variablesStr.forEachIndexed{index, el ->        //перевод всех элементов массива в целочисленный формат
            variablesInt[index] = el.toInt()
        }
        return variablesInt
    }

    //функция удаления элемента из массива чисел
    private fun removeItemArrayInt(InputArray:Array <Int>, IndexRemove: Int): Array<Int>{
        var resultArray = Array<Int>(InputArray.size - 1) {0}
        resultArray.forEachIndexed{index, el ->
            if (index >= IndexRemove) {
                resultArray[index] = InputArray[index + 1]
            }
            else {
                resultArray[index] = InputArray[index]
            }
        }
        return resultArray
    }

    //функция удаления элемента из массива строк
    private fun removeItemArrayString(InputArray:Array <String>, IndexRemove: Int): Array<String>{
        var resultArray = Array<String>(InputArray.size - 1) {""}
        resultArray.forEachIndexed{index, el ->
            if (index >= IndexRemove) {
                resultArray[index] = InputArray[index + 1]
            }
            else {
                resultArray[index] = InputArray[index]
            }
        }
        return resultArray
    }

    //Функция делает вычисления, исходя из приоритетов операций, и перебирая массивы элементов
    private fun calculation(Variables: Array<Int>, Operands: Array<String>, OrderOperands: Array<Int>): String{
        var resultStr : String = ""
        var tmpVarArray = Variables
        var tmpOrderOperands = OrderOperands
        var tmpOperands = Operands
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
                if (el > tmpOrderOperands[indexOrder]) {
                    tmpOrderOperands[index]--
                }
            }
        }
        resultStr = tmpVarArray[0].toString()
        return resultStr
    }

    //Функция проверят наличие унарного минуса перед переменной, удаляет операнд из массива операндов
    private fun parseUnaryMinusOperands(Operands:Array <String>, IndexOperands: Array <Int>):Array <String>{
        var tmpOperands = Operands
        if (Operands[0] == "-" && IndexOperands[0] == 0){       //проверяем начало массива операндов, если там минус
            tmpOperands = removeItemArrayString(tmpOperands, 0)       //то для удалить первый операнд из массива
        }
        IndexOperands.forEachIndexed{index, el ->               //пербор элементов массива индексов операндов
            if (index <= IndexOperands.lastIndex - 1){          //если при переборе индексов, индекс не последний, то выполнять проверку на разницу индексов ()
                if (IndexOperands[index + 1] - el == 1 && (Operands[index + 1] == "-"))              //если разница между индексами равна единице И второй операнд - минус,
                    tmpOperands = removeItemArrayString(tmpOperands, index + 1)                    //то удалить операнд из массива
            }
        }
        return tmpOperands
    }
    //Функция проверят наличие унарного минуса перед переменной, удаляет из массива индексов операндов
    private fun parseUnaryMinusIndexOperands(Operands:Array <String>, IndexOperands: Array <Int>):Array <Int>{
        var tmpIndexOperands = IndexOperands
        if (Operands[0] == "-" && IndexOperands[0] == 0){       //проверяем начало массива операндов, если там минус
            tmpIndexOperands = removeItemArrayInt(tmpIndexOperands, 0)       //то для удалить первый  индекс операнда из массива
        }
        IndexOperands.forEachIndexed{index, el ->               //пербор элементов массива индексов операндов
            if (index <= IndexOperands.lastIndex - 1){          //если при переборе индексов, индекс не последний, то выполнять проверку на разницу индексов ()
                if (IndexOperands[index + 1] - el == 1 && (Operands[index + 1] == "-"))              //если разница между индексами равна единице И второй операнд - минус,
                    tmpIndexOperands = removeItemArrayInt(tmpIndexOperands, index + 1)                    //то удалить индекс операнда из массива
            }
        }
        return tmpIndexOperands
    }

    //Функция принимает на вход строку выражения калькулятора, и парсит её на переменные и операнды для выполнения отдельных операций
    private fun doExpression(Expression: String): String {
        var result: String = ""                     //строковая переменная с результатом

        var operandsArray: Array<String> = parseOperands(Expression)      //вызов функции назначения массива операндов
        var indexOperandsArray: Array<Int> = parseOperandIndexFromExpressionArray(Expression, operandsArray)  //вызов функции назначения массива индексов операндов
        var tmpOperandsArray = parseUnaryMinusOperands(operandsArray, indexOperandsArray)                          //вызов функции удаления операнда из массива операндов, и присвоит его временному массиву, т.к. основной массив ещё пригодится
        var tmpIndexOperandsArray = parseUnaryMinusIndexOperands(operandsArray, indexOperandsArray)             //вызов функции удаления из массива индексов операндов унарных минусов, и присвоения временному массиву
        indexOperandsArray = tmpIndexOperandsArray
        operandsArray = tmpOperandsArray

        var variableArray : Array<Int> = parseVariablesArray(Expression, indexOperandsArray)           //массив переменных
        //variableArray = parseUnaryMinusVariables(variableArray, operandsArray, indexOperandsArray)          //вызов функции обработки унарного минуса, переприсвоение массива операндов и переменных

        var orderOperandsArray : Array<Int> = orderOfOperands(operandsArray)   //вызов функции назначения массива индексов порядка операндов
        result = calculation(variableArray, operandsArray, orderOperandsArray)

        return result
    }

    //Функция на вход принимает два агрумента и операнд, затем производит операцию
    private fun operationVariables(VarA: Int, VarB: Int, Operand: String): Int {
        var result: Int = 0        //переменная будет выводить результат
        result = when (Operand) {     //оператор выбора. в зависимости от используемого операнда, будет вычислен результат
            "+" -> VarA + VarB       //если сложение, то выполнить операцию сложения
            "-" -> VarA - VarB       //если вычитание, то выполнить вычитание
            "*" -> VarA * VarB       //если умножение, то выполнить умножение
            "/" -> (VarA / VarB) - (VarA % VarB)       //если деление, то вернуть целочисленный остаток от деления
            else -> 0
        }
        return result
    }
}