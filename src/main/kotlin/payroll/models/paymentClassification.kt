package payroll.models

import payroll.isInInterval
import java.time.LocalDate

interface PaymentClassification {
    fun calculatePay(payInterval: Interval): Double
}

data class SalariedClassification(
    val salary: Double
): PaymentClassification {
    override fun calculatePay(payInterval: Interval): Double {
        return salary
    }
}

data class HourlyClassification(
        val hourlyRate: Double,
        val timeCards: MutableList<TimeCard> = mutableListOf()
): PaymentClassification {
    override fun calculatePay(payInterval: Interval): Double {

        var totalPay = 0.0

        timeCards.forEach {
            if (isTimeCardInPayPeriod(it, payInterval)) {
                totalPay +=calculatePayTimeCard(it)
            }
        }

        return totalPay

    }

    private fun calculatePayTimeCard(timeCard: TimeCard): Double {
        val basePayHours = Math.min(8, timeCard.hours)
        val overtimeHour = Math.max(0, timeCard.hours - basePayHours)

        return hourlyRate * basePayHours + hourlyRate * (1.5) * overtimeHour
    }

    private fun isTimeCardInPayPeriod(timeCard: TimeCard, interval: Interval): Boolean {
        return isInInterval(timeCard.date, interval)
    }

}

data class CommissionedClassification(
        val commissionRate: Double,
        val salary: Double,
        val salesReceipts: MutableList<SalesReceipt> = mutableListOf()
): PaymentClassification {
    override fun calculatePay(payInterval: Interval): Double {
        var totalPay =  salary

        salesReceipts.forEach {
            if (isSalesReceiptInPayPeriod(it, payInterval)) {
                totalPay += commissionRate * it.amount
            }
        }

        return totalPay
    }

    private fun isSalesReceiptInPayPeriod(receipt: SalesReceipt, payInterval: Interval): Boolean {
        return isInInterval(receipt.date, payInterval)
    }
}

data class TimeCard(
        val date: LocalDate,
        val hours: Int
)

data class SalesReceipt(
        val date: LocalDate,
        val amount: Double
)