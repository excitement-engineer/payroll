package payroll.transaction.changeEmployee.changeClassification

import payroll.Database
import payroll.ID
import payroll.models.Employee
import payroll.models.PaymentClassification
import payroll.models.PaymentSchedule
import payroll.transaction.changeEmployee.ChangeEmployeeTransaction

abstract class ChangeEmployeeClassificationTransaction(
        empId: ID,
        db: Database
): ChangeEmployeeTransaction(empId, db) {
    override fun change(employee: Employee) {
        employee.paymentClassification = getClassification()
        employee.paymentSchedule = getPaymentSchedule()
    }

    abstract fun getClassification(): PaymentClassification
    abstract fun getPaymentSchedule(): PaymentSchedule
}