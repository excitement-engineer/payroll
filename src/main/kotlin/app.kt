import payroll.InMemoryDatabase
import payroll.PayrollApplication
import payroll.source.TextParserTransactionSource
import payroll.transaction.TransactionFactoryImplementation

fun main(args : Array<String>) {
    val db = InMemoryDatabase()

    val inputStream = System.`in`

    val transactionFactory = TransactionFactoryImplementation(db)
    val transactionSource = TextParserTransactionSource(inputStream, transactionFactory)

    val application = PayrollApplication(transactionSource, db)

    application.run()
}