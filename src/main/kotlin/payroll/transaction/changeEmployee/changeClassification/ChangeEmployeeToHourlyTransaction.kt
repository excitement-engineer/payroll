package payroll.transaction.changeEmployee.changeClassification

import payroll.Database
import payroll.ID
import payroll.models.*

class ChangeEmployeeToHourlyTransaction(
        empId: ID,
        private val hourlyRate: Double,
        db: Database
): ChangeEmployeeClassificationTransaction(empId, db) {
    override fun getPaymentSchedule(): PaymentSchedule {
        return WeeklySchedule
    }

    override fun getClassification(): PaymentClassification {
        return HourlyClassification(hourlyRate)
    }
}