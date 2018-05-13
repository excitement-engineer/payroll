package payroll.transaction.changeEmployee.changePaymentMethod

import payroll.Database
import payroll.ID
import payroll.models.HoldPaycheck
import payroll.models.PaymentMethod

class ChangeToHoldTransaction(
        empId: ID,
        db: Database
): ChangePaymentMethodTransaction(empId, db) {
    override fun getPaymentMethod(): PaymentMethod {
        return HoldPaycheck
    }
}