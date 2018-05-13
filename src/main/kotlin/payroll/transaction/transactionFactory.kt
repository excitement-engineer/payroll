package payroll.transaction

import payroll.Database
import payroll.transaction.addNewEmployee.AddHourlyEmployeeTransaction

interface TransactionFactory {

    fun makeAddHourlyEmployeeTransaction(empId: String, name: String, address: String, hourlyRate: Double): Transaction

    // Add make functions for other transactions
}

class TransactionFactoryImplementation(
        private val db: Database
): TransactionFactory {
    override fun makeAddHourlyEmployeeTransaction(empId: String, name: String, address: String, hourlyRate: Double): Transaction {
        return AddHourlyEmployeeTransaction(
                empId,
                name,
                address,
                hourlyRate,
                db
        )
    }
}