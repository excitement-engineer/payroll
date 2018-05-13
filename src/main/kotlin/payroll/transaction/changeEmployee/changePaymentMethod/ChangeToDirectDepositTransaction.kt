package payroll.transaction.changeEmployee.changePaymentMethod

import payroll.Database
import payroll.ID
import payroll.models.DirectDeposit
import payroll.models.PaymentMethod

class ChangeToDirectDepositTransaction(
        empId: ID,
        private val bank: String,
        private val account: String,
        db: Database
): ChangePaymentMethodTransaction(empId, db) {
    override fun getPaymentMethod(): PaymentMethod {
        return DirectDeposit(
                bank = bank,
                account = account
        )
    }
}