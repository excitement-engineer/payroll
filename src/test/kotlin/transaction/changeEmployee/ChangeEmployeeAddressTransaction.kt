package transaction.changeEmployee

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.Employee
import payroll.models.HoldPaycheck
import payroll.models.HourlyClassification
import payroll.models.MonthlySchedule
import payroll.transaction.changeEmployee.ChangeEmployeeAddressTransaction
import kotlin.test.assertEquals

class ChangeEmployeeAddressTransactionTest {

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

        val transaction = ChangeEmployeeAddressTransaction(
                empId = "1",
                address = "Boston",
                db = database
        )

        transaction.execute()

        assertEquals(
                expected = "Boston",
                actual = employee.address
        )


    }
}