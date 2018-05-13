package transaction.changeEmployee.changeClassification

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.changeEmployee.changeClassification.ChangeEmployeeToHourlyTransaction
import kotlin.test.assertEquals

class ChangeEmployeeToHourlyTransactionTest {

    @Test
    fun testTransaction() {
            val database = InMemoryDatabase()

            val employee = Employee(
                    id = "1",
                    name = "Bob",
                    address = "New York",
                    paymentMethod = HoldPaycheck,
                    paymentClassification = SalariedClassification(20000.0),
                    paymentSchedule = MonthlySchedule,
                    unionMembership = null
            )

            database.addEmployee(employee)

            val transaction = ChangeEmployeeToHourlyTransaction(
                    empId = "1",
                    hourlyRate = 10.0,
                    db = database
            )

            transaction.execute()

            assertEquals(
                    expected = HourlyClassification(hourlyRate = 10.0),
                    actual = employee.paymentClassification
            )

            assertEquals(
                    expected = WeeklySchedule,
                    actual = employee.paymentSchedule
            )
    }
}