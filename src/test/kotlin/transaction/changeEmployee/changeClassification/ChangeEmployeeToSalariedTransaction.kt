package transaction.changeEmployee.changeClassification

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.changeEmployee.changeClassification.ChangeEmployeeToSalariedTransaction
import kotlin.test.assertEquals

class ChangeEmployeeToSalariedTransactionTest {

    @Test
    fun testTransaction() {
        val database = InMemoryDatabase()

        val employee = Employee(
                id = "1",
                name = "Bob",
                address = "New York",
                paymentMethod = HoldPaycheck,
                paymentClassification = HourlyClassification(10.0),
                paymentSchedule = WeeklySchedule,
                unionMembership = null
        )

        database.addEmployee(employee)

        val transaction = ChangeEmployeeToSalariedTransaction(
                empId = "1",
                salary = 20000.0,
                db = database
        )

        transaction.execute()

        assertEquals(
                expected = SalariedClassification(20000.0),
                actual = employee.paymentClassification
        )

        assertEquals(
                expected = MonthlySchedule,
                actual = employee.paymentSchedule
        )
    }
}