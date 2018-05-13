package payroll.source

import payroll.transaction.Transaction

interface TransactionSource {
    fun getTransaction(): Transaction?
}