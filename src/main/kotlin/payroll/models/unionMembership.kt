package payroll.models

import payroll.isInInterval
import java.time.DayOfWeek
import java.time.LocalDate

data class UnionMembership(
        val memberId: String,
        val dues: Double,
        val serviceCharges: MutableList<UnionServiceCharge> = mutableListOf()
) {

    fun calculateDeductions(interval: Interval): Double {

        val duesDeduction = numberOfFridaysInPeriod(interval) * dues

        return duesDeduction + calculateServiceCharges(interval)
    }

    private fun calculateServiceCharges(interval: Interval): Double {
        var serviceChargesTotal =0.0

        serviceCharges.forEach {
            if (isInInterval(it.date, interval)) {
                serviceChargesTotal +=it.amount
            }
        }

        return serviceChargesTotal
    }

    private fun numberOfFridaysInPeriod(interval: Interval): Int {

        val (start, end ) = interval

        var date = start

        var fridayCount = 0
        while (date <= end) {
            if (date.dayOfWeek=== DayOfWeek.FRIDAY) {
                fridayCount++
            }
            date = date.plusDays(1)
        }

        return fridayCount
    }
}

data class UnionServiceCharge(
        val amount: Double,
        val date: LocalDate
)