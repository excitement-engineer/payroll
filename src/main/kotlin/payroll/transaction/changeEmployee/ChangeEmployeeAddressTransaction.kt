package payroll.transaction.changeEmployee

import payroll.Database
import payroll.models.Employee

class ChangeEmployeeAddressTransaction(
        empId: String,
        private val address: String,
        db: Database
): ChangeEmployeeTransaction(empId, db) {
    override fun change(employee: Employee) {
        employee.address = address
    }
}