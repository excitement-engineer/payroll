package payroll.transaction.changeEmployee.changeClassification

import payroll.Database
import payroll.ID
import payroll.models.MonthlySchedule
import payroll.models.PaymentClassification
import payroll.models.PaymentSchedule
import payroll.models.SalariedClassification

class ChangeEmployeeToSalariedTransaction(
        empId: ID,
        private val salary: Double,
        db: Database
): ChangeEmployeeClassificationTransaction(empId, db) {
    override fun getClassification(): PaymentClassification {
        return SalariedClassification(salary)
    }

    override fun getPaymentSchedule(): PaymentSchedule {
        return MonthlySchedule
    }
}