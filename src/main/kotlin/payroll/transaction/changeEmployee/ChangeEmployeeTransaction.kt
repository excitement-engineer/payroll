package payroll.transaction.changeEmployee

import payroll.Database
import payroll.ID
import payroll.models.Employee
import payroll.transaction.Transaction

abstract class ChangeEmployeeTransaction(private val empId: ID, private val db: Database): Transaction {
    override fun execute() {
        val employee = db.getEmployee(empId)

        if (employee == null) {
            throw Exception("No employee for id $empId")
        }

        change(employee)

    }

    abstract fun change(employee: Employee)
}