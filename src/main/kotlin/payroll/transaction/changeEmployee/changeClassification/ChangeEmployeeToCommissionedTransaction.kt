package payroll.transaction.changeEmployee.changeClassification

import payroll.Database
import payroll.ID
import payroll.models.BiWeeklySchedule
import payroll.models.CommissionedClassification
import payroll.models.PaymentClassification
import payroll.models.PaymentSchedule

class ChangeEmployeeToCommissionedTransaction(
        private val empId: ID,
        private val salary: Double,
        private val rate: Double,
        private val db: Database
): ChangeEmployeeClassificationTransaction(empId, db) {
    override fun getClassification(): PaymentClassification {
        return CommissionedClassification(
                salary = salary,
                commissionRate = rate
        )
    }

    override fun getPaymentSchedule(): PaymentSchedule {
        return BiWeeklySchedule
    }
}