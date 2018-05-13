package payroll.models

import java.time.LocalDate

data class Employee(
        val id: String,
        var name: String,
        var address: String,
        var paymentClassification: PaymentClassification,
        var unionMembership: UnionMembership?,
        var paymentMethod: PaymentMethod,
        var paymentSchedule: PaymentSchedule
) {

    fun calculatePay(payInterval: Interval): PayCheck {
        val grossPay = paymentClassification.calculatePay(payInterval)
        val deductions = unionMembership?.calculateDeductions(payInterval) ?: 0.0

        val netPay = grossPay - deductions

        return PayCheck(
                payInterval = payInterval,
                grossPay = grossPay,
                netPay = netPay,
                deductions = deductions,
                paymentMethod = paymentMethod
        )
    }

    fun getPayPeriodStartDate(payDate: LocalDate): LocalDate {
        return paymentSchedule.getPayPeriodStartDate(payDate)
    }

    fun isPayDay(date: LocalDate): Boolean {
        return paymentSchedule.isPayDay(date)
    }
}
