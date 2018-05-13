package payroll.transaction.changeEmployee.changePaymentMethod

import payroll.Database
import payroll.models.Employee
import payroll.models.PaymentMethod
import payroll.transaction.changeEmployee.ChangeEmployeeTransaction

abstract class ChangePaymentMethodTransaction(
        empId: String, database: Database
): ChangeEmployeeTransaction(empId, database) {
    override fun change(employee: Employee) {
        employee.paymentMethod = getPaymentMethod()
    }

    abstract fun getPaymentMethod(): PaymentMethod
}