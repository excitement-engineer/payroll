package transaction

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.PostSalesReceiptTransaction
import payroll.transaction.PostTimeCardTransaction
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFails

class PostSalesReceiptTransactionTest {

    val employee = Employee(
            id = "1",
            name = "Bob",
            address = "New York",
            paymentMethod = HoldPaycheck,
            paymentClassification = CommissionedClassification(20.0, 80_000.0),
            paymentSchedule = MonthlySchedule,
            unionMembership = null
    )

    @Test
    fun testTransaction() {

        val database = InMemoryDatabase()

        database.addEmployee(employee)

        val transaction = PostSalesReceiptTransaction(
                empId = "1",
                date = LocalDate.of(2017, 1, 1),
                amount = 100.0,
                db = database
        )

        transaction.execute()

        val employee = database.getEmployee("1")!!
        val receipts = (employee.paymentClassification as CommissionedClassification).salesReceipts

        assertEquals(1, receipts.size)
        assertEquals(
                SalesReceipt(LocalDate.of(2017, 1, 1), 100.0),
                receipts[0]
        )
    }


    @Test
    fun testErrorUnknownEmpID() {
        val database = InMemoryDatabase()

        val transaction = PostSalesReceiptTransaction(
                empId = "2",
                date = LocalDate.now(),
                amount = 6.0,
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
    fun testErrorNotCommissioned() {
        val database = InMemoryDatabase()

        val employeeSalaried = employee.copy(
                paymentClassification = SalariedClassification(80_000.0)
        )

        database.addEmployee(employeeSalaried)

        val transaction = PostSalesReceiptTransaction(
                empId = "1",
                date = LocalDate.of(2017, 1, 1),
                amount = 5.0,
                db = database
        )

        val error = assertFails {
            transaction.execute()
        }

        assertEquals(
                "The selected employee is not commissioned",
                error.message
        )
    }
}