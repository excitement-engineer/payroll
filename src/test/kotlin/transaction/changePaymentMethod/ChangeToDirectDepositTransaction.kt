package transaction.changePaymentMethod

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.changeEmployee.changePaymentMethod.ChangeToDirectDepositTransaction
import kotlin.test.assertEquals

class ChangeToDirectDepositTransactionTest {
    @Test
    fun testTransaction() {
        val database = InMemoryDatabase()

        val employee = Employee(
                id = "1",
                name = "Bob",
                address = "New York",
                paymentMethod = HoldPaycheck,
                paymentClassification = HourlyClassification(20.0),
                paymentSchedule = MonthlySchedule,
                unionMembership = null
        )

        database.addEmployee(employee)

        val transaction = ChangeToDirectDepositTransaction(
                empId = "1",
                bank = "Rabobank",
                account = "NL17RABO0123",
                db = database
        )

        transaction.execute()

        assertEquals(
                expected = DirectDeposit(bank = "Rabobank", account = "NL17RABO0123"),
                actual = employee.paymentMethod
        )


    }
}