package payroll.transaction.changeEmployee.changePaymentMethod

import payroll.Database
import payroll.ID
import payroll.models.Employee
import payroll.models.MailPaycheck
import payroll.models.PaymentMethod
import payroll.transaction.changeEmployee.ChangeEmployeeTransaction

class ChangeToMailPaycheckTransaction(
        empId: ID,
        private val address: String,
        db: Database
): ChangePaymentMethodTransaction(empId, db) {
    override fun getPaymentMethod(): PaymentMethod {
        return MailPaycheck(address)
    }

}