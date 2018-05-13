package payroll.transaction.addNewEmployee

import payroll.Database
import payroll.models.BiWeeklySchedule
import payroll.models.CommissionedClassification
import payroll.models.Employee
import payroll.models.HoldPaycheck
import payroll.transaction.Transaction

class AddCommissionedEmployeeTransaction(
    val empId: String,
    val name: String,
    val address: String,
    val salary: Double,
    val commissionRate: Double,
    val database: Database
): Transaction {
    override fun execute() {
        val employee = Employee(
                id = empId,
                name = name,
                address = address,
                paymentClassification = CommissionedClassification(commissionRate, salary),
                paymentSchedule = BiWeeklySchedule,
                unionMembership = null,
                paymentMethod = HoldPaycheck
        )

        database.addEmployee(employee)
    }

}