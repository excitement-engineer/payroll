package payroll.models

interface PaymentMethod

object HoldPaycheck: PaymentMethod

data class DirectDeposit(
        val bank: String,
        val account: String
): PaymentMethod

data class MailPaycheck(
        val address: String
): PaymentMethod