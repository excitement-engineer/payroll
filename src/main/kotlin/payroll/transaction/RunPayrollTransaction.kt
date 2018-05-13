package payroll.transaction

import payroll.Database
import payroll.ID
import payroll.models.Employee
import payroll.models.PayCheck
import java.time.LocalDate

class RunPayrollTransaction(
        private val date: LocalDate,
        private val database: Database
): Transaction {

    private val paychecks: MutableMap<ID, PayCheck> = mutableMapOf()

    override fun execute() {

        val employees = database.getEmployees()

        employees.forEach {

            if (it.isPayDay(date)) {

                val startPayInterval = it.getPayPeriodStartDate(date)

                val payInterval = Pair(startPayInterval, date)

                val payCheck = it.calculatePay(payInterval)
                paychecks[it.id] = payCheck
            }
        }

    }

    fun getPaycheck(employeeId: ID): PayCheck? {
        return paychecks[employeeId]
    }
}