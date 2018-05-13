package payroll.transaction.changeEmployee

import payroll.Database
import payroll.ID
import payroll.models.Employee

class ChangeEmployeeNameTransaction(
        empId: ID,
        private val name: String,
        db: Database
): ChangeEmployeeTransaction(empId, db) {

    override fun change(employee: Employee) {
        employee.name = name
    }
}