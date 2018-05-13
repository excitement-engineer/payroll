package payroll.transaction.addNewEmployee

import payroll.Database
import payroll.models.Employee
import payroll.models.HoldPaycheck
import payroll.models.HourlyClassification
import payroll.models.WeeklySchedule
import payroll.transaction.Transaction

class AddHourlyEmployeeTransaction(
        val empId: String,
        val name: String,
        val address: String,
        val hourlyRate: Double,
        val database: Database
): Transaction {
    override fun execute() {
        val employee = Employee(
                id = empId,
                name = name,
                address = address,
                paymentMethod = HoldPaycheck,
                paymentClassification = HourlyClassification(hourlyRate),
                unionMembership = null,
                paymentSchedule = WeeklySchedule
        )

        database.addEmployee(employee)
    }
}