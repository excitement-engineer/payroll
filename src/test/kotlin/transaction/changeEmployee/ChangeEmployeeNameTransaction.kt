package transaction.changeEmployee

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.Employee
import payroll.models.HoldPaycheck
import payroll.models.HourlyClassification
import payroll.models.MonthlySchedule
import payroll.transaction.changeEmployee.ChangeEmployeeNameTransaction
import kotlin.test.assertEquals

class ChangeEmployeeNameTransactionTest {

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

        val transaction = ChangeEmployeeNameTransaction(
                empId = "1",
                name = "Jack",
                db = database
        )

        transaction.execute()

        assertEquals(
                expected = "Jack",
                actual = employee.name
        )


    }
}