package payroll.transaction

import payroll.Database

class DeleteEmployeeTransaction(
        private val empId: String,
        private val db: Database
): Transaction {
    override fun execute() {

        val employee = db.getEmployee(empId)

        if (employee == null) {
            throw Exception("No employee for id $empId")
        }

        db.removeEmployee(employee)
    }

}