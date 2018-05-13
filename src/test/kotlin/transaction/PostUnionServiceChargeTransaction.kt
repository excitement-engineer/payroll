package transaction

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.PostUnionServiceChargeTransaction
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFails

class PostUnionServiceChargeTransactionTest {


    val unionMembership =UnionMembership(
            memberId = "1",
            dues = 1000.0
    )

    val employee = Employee(
            id = "1",
            name = "Bob",
            address = "New York",
            paymentMethod = HoldPaycheck,
            paymentClassification = CommissionedClassification(20.0, 80_000.0),
            paymentSchedule = MonthlySchedule,
            unionMembership = unionMembership
    )

    @Test
    fun testTransaction() {
        val db = InMemoryDatabase()

        db.addEmployee(employee)
        db.addUnionMember("1", employee)

        val transaction = PostUnionServiceChargeTransaction(
                memberId = "1",
                date = LocalDate.of(2018, 1, 1),
                amount = 100.0,
                db = db
        )

        transaction.execute()

        assertEquals(
                expected = UnionServiceCharge(
                        date = LocalDate.of(2018, 1, 1),
                        amount = 100.0
                ),
                actual = unionMembership.serviceCharges[0]
        )

    }

    @Test
    fun testNoMember() {
        val db = InMemoryDatabase()

        val employeeWithoutMember = employee.copy(
                unionMembership = null
        )

        db.addEmployee(employeeWithoutMember)

        val transaction = PostUnionServiceChargeTransaction(
                memberId = "1",
                date = LocalDate.of(2018, 1, 1),
                amount = 100.0,
                db = db
        )

        val error = assertFails {
            transaction.execute()
        }

        assertEquals(
                expected = "No union member for id 1",
                actual = error.message
        )
    }

    @Test
    fun testNoUnionMembership() {
        val db = InMemoryDatabase()

        val employeeWithoutMember = employee.copy(
                unionMembership = null
        )

        db.addEmployee(employeeWithoutMember)
        db.addUnionMember("1", employeeWithoutMember)

        val transaction = PostUnionServiceChargeTransaction(
                memberId = "1",
                date = LocalDate.of(2018, 1, 1),
                amount = 100.0,
                db = db
        )

        val error = assertFails {
            transaction.execute()
        }

        assertEquals(
                expected = "Union member with id 1 has no union membership",
                actual = error.message
        )
    }
}