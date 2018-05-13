package payroll

import payroll.source.TransactionSource

class PayrollApplication(
        val transactionSource: TransactionSource,
        val db: Database
) {
    fun run() {
        var transaction = transactionSource.getTransaction()

        while (transaction != null) {

            transaction.execute()

            println("Executing transaction $transaction")

            transaction = transactionSource.getTransaction()
        }
    }
}