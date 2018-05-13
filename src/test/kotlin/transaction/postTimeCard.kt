package transaction

import org.junit.Before
import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.DeleteEmployeeTransaction
import payroll.transaction.PostTimeCardTransaction
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFails

class PostTimeCardTransactionTest {

    val employee = Employee(
            id = "1",
            name = "Bob",
            address = "New York",
            paymentMethod = HoldPaycheck,
            paymentClassification = HourlyClassification(20.0),
            paymentSchedule = MonthlySchedule,
            unionMembership = null
    )

    @Test
    fun testTransaction() {

        val database = InMemoryDatabase()

        database.addEmployee(employee)

        val transaction = PostTimeCardTransaction(
                empId = "1",
                date = LocalDate.of(2017, 1, 1),
                hours = 5,
                db = database
        )

        transaction.execute()

        val employee = database.getEmployee("1")!!
        val timeCards = (employee.paymentClassification as HourlyClassification).timeCards

        assertEquals(1, timeCards.size)
        assertEquals(
                TimeCard(LocalDate.of(2017, 1, 1), 5),
                timeCards[0]
        )
    }


    @Test
    fun testErrorUnknownEmpID() {
        val database = InMemoryDatabase()

        val transaction = PostTimeCardTransaction(
                empId = "2",
                date = LocalDate.now(),
                hours = 6,
                db = database
        )

        val error = assertFails {
            transaction.execute()
        }

        assertEquals(
                "No employee for id 2",
                error.message
        )
    }

    @Test
    fun testErrorNotHourly() {
        val database = InMemoryDatabase()

        val employeeSalaried = employee.copy(
                paymentClassification = SalariedClassification(80_000.0)
        )

        database.addEmployee(employeeSalaried)

        val transaction = PostTimeCardTransaction(
                empId = "1",
                date = LocalDate.of(2017, 1, 1),
                hours = 5,
                db = database
        )

        val error = assertFails {
            transaction.execute()
        }

        assertEquals(
                "The selected employee is not hourly",
                error.message
        )
    }
}