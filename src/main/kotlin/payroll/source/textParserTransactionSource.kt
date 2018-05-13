package payroll.source

import payroll.transaction.Transaction
import payroll.transaction.TransactionFactory
import java.io.InputStream
import java.util.*
import java.util.regex.Pattern


class TextParserTransactionSource(
 inputStream: InputStream,
 private val transactionFactory: TransactionFactory
): TransactionSource {

    private val scanner = Scanner(inputStream)

    private lateinit var inputParts: List<String>

    override fun getTransaction(): Transaction? {

        val input = scanner.nextLine()

        if (input == "") {
            return null
        }

        inputParts = splitString(input)

       return parseInput()
    }

    private fun parseInput(): Transaction? {
        return when(inputParts[0]) {
            "AddEmp" -> parseAddEmployee()
            else -> {
                throw Exception("Incorrect transaction structure")
            }
        }
    }

    private fun parseAddEmployee(): Transaction? {

        return when(inputParts[4]) {
            "H" -> transactionFactory.makeAddHourlyEmployeeTransaction(
                    empId = inputParts[1],
                    name = parseString(inputParts[2]),
                    address = parseString(inputParts[3]),
                    hourlyRate = parseDouble(inputParts[5])
            )
            else -> {
                throw Exception("Incorrect transaction structure")
            }
        }
    }

    private fun parseDouble(input: String): Double {
        return input.toDouble()
    }

    private fun parseString(input: String): String {
        return input.substring(1, input.length - 1)
    }

    private fun splitString(input: String): List<String> {
        val matchList = mutableListOf<String>()
        val regex = Pattern.compile("[^\\s\"]+|\"([^\"]*)\"")
        val regexMatcher = regex.matcher(input)
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group())
        }

        return matchList

    }
}