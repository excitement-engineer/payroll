package payroll.transaction.addNewEmployee

import payroll.Database
import payroll.models.*
import payroll.transaction.Transaction

class AddSalariedEmployeeTransaction(
        val empId: String,
        val name: String,
        val address: String,
        val salary: Double,
        val database: Database
): Transaction {
    override fun execute() {

        val employee = Employee(
                id = empId,
                name = name,
                address = address,
                paymentClassification = SalariedClassification(salary),
                paymentSchedule = MonthlySchedule,
                unionMembership = null,
                paymentMethod = HoldPaycheck
        )

        database.addEmployee(employee)
    }
}

