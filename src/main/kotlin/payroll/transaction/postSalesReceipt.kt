package payroll.transaction

import payroll.Database
import payroll.models.CommissionedClassification
import payroll.models.SalesReceipt
import java.time.LocalDate

class PostSalesReceiptTransaction(
        private val empId: String,
        private val date: LocalDate,
        private val amount: Double,
        private val db: Database
): Transaction {
    override fun execute() {
        val employee = db.getEmployee(empId)

        if (employee == null) {
            throw Exception("No employee for id $empId")
        }

        val classification = employee.paymentClassification

        if (classification is CommissionedClassification) {
            val receipt = SalesReceipt(date, amount)
            classification.salesReceipts.add(receipt)
        } else {
            throw Exception("The selected employee is not commissioned")
        }
    }
}