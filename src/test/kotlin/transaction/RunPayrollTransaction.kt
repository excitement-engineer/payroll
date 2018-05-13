package transaction

import org.junit.Test
import payroll.InMemoryDatabase
import payroll.models.*
import payroll.transaction.PostSalesReceiptTransaction
import payroll.transaction.PostTimeCardTransaction
import payroll.transaction.PostUnionServiceChargeTransaction
import payroll.transaction.RunPayrollTransaction
import payroll.transaction.addNewEmployee.AddCommissionedEmployeeTransaction
import payroll.transaction.addNewEmployee.AddHourlyEmployeeTransaction
import payroll.transaction.addNewEmployee.AddSalariedEmployeeTransaction
import payroll.transaction.changeEmployee.changeUnionMembership.PutEmployeeInUnionTransaction
import java.time.LocalDate
import kotlin.test.assertEquals

class RunPayrollTransactionTest {

    @Test
    fun testSalariedEmployee() {

        val database = InMemoryDatabase()

        val salariedEmployeeTransaction = AddSalariedEmployeeTransaction(
                empId = "1",
                salary = 1000.0,
                name = "Bob",
                address = "New York",
                database = database
        )

        salariedEmployeeTransaction.execute()

        val payDate = LocalDate.of(2011, 11, 30)

        val transaction = RunPayrollTransaction(
                date = payDate,
                database = database
        )

        transaction.execute()

        assertEquals(
                expected = PayCheck(
                    deductions = 0.0,
                    payInterval = Pair(
                            LocalDate.of(2011, 11, 1),
                            payDate
                    ),
                    netPay = 1000.0,
                    grossPay = 1000.0,
                    paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testSalariedEmployeeNotPayDate() {
        val database = InMemoryDatabase()

        val salariedEmployeeTransaction = AddSalariedEmployeeTransaction(
                empId = "1",
                salary = 1000.0,
                name = "Bob",
                address = "New York",
                database = database
        )

        salariedEmployeeTransaction.execute()

        val transaction = RunPayrollTransaction(
                date = LocalDate.of(2011, 11, 16),
                database = database
        )

        transaction.execute()

        assertEquals(
                expected = null,
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testHourlyEmployeeNoTimeCards() {
        val database = InMemoryDatabase()

        val addHourlyEmployee = AddHourlyEmployeeTransaction(
                empId = "1",
                database = database,
                address = "New York",
                name = "Bob",
                hourlyRate = 10.0
        )

        addHourlyEmployee.execute()

        val payDate = LocalDate.of(2018, 5, 4)
        val transaction = RunPayrollTransaction(
                database = database,
                date = LocalDate.of(2018, 5, 4)
        )

        transaction.execute()

        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2018, 4, 29),
                                payDate
                        ),
                        netPay = 0.0,
                        grossPay = 0.0,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testHourlyEmployeeOneTimeCard() {
        val database = InMemoryDatabase()

        val addHourlyEmployee = AddHourlyEmployeeTransaction(
                empId = "1",
                database = database,
                address = "New York",
                name = "Bob",
                hourlyRate = 15.25
        )

        addHourlyEmployee.execute()

        val payDate = LocalDate.of(2018, 5, 4);

        val addTimeCard = PostTimeCardTransaction(
                empId = "1",
                date = payDate,
                hours = 2,
                db = database
        )

        addTimeCard.execute()

        val transaction = RunPayrollTransaction(
                database = database,
                date = payDate
        )

        transaction.execute()

        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2018, 4, 29),
                                payDate
                        ),
                        netPay = 30.50,
                        grossPay = 30.50,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testHourlyEmployeeOneTimeCardOverTime() {
        val database = InMemoryDatabase()

        val addHourlyEmployee = AddHourlyEmployeeTransaction(
                empId = "1",
                database = database,
                address = "New York",
                name = "Bob",
                hourlyRate = 15.25
        )

        addHourlyEmployee.execute()

        val payDate = LocalDate.of(2018, 5, 4);

        val addTimeCard = PostTimeCardTransaction(
                empId = "1",
                date = payDate,
                hours = 9,
                db = database
        )

        addTimeCard.execute()

        val transaction = RunPayrollTransaction(
                database = database,
                date = payDate
        )

        transaction.execute()

        val pay = (8 + 1.5) * 15.25
        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2018, 4, 29),
                                payDate
                        ),
                        netPay = pay,
                        grossPay = pay,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testPayHourlyEmployeeOnWrongDate() {
        val database = InMemoryDatabase()
        val addHourlyEmployee = AddHourlyEmployeeTransaction(
                empId = "1",
                database = database,
                address = "New York",
                name = "Bob",
                hourlyRate = 15.25
        )

        addHourlyEmployee.execute()

        // Thursday
        val payDate = LocalDate.of(2018, 5, 3);

        val transaction = RunPayrollTransaction(
                database = database,
                date = payDate
        )

        transaction.execute()

        assertEquals(
                expected = null,
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testPayHourlyEmployeeTwoTimeCards() {
        val database = InMemoryDatabase()

        val addHourlyEmployee = AddHourlyEmployeeTransaction(
                empId = "1",
                database = database,
                address = "New York",
                name = "Bob",
                hourlyRate = 15.25
        )

        addHourlyEmployee.execute()

        val payDate = LocalDate.of(2018, 5, 4);

        PostTimeCardTransaction(
                empId = "1",
                date = payDate,
                hours = 2,
                db = database
        ).execute()

        PostTimeCardTransaction(
                empId = "1",
                date = payDate.minusDays(1),
                hours = 10,
                db = database
        ).execute()


        val transaction = RunPayrollTransaction(
                database = database,
                date = payDate
        )

        transaction.execute()

        val pay = (2 + 8 + 2 * 1.5) * 15.25

        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2018, 4, 29),
                                payDate
                        ),
                        netPay = pay,
                        grossPay = pay,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testPayHourEmployeeWithTimeCardsSpanningTwoPeriods() {
        val database = InMemoryDatabase()

        val addHourlyEmployee = AddHourlyEmployeeTransaction(
                empId = "1",
                database = database,
                address = "New York",
                name = "Bob",
                hourlyRate = 15.25
        )

        addHourlyEmployee.execute()

        val payDate = LocalDate.of(2018, 5, 4);

        PostTimeCardTransaction(
                empId = "1",
                date = payDate,
                hours = 2,
                db = database
        ).execute()

        PostTimeCardTransaction(
                empId = "1",
                date = payDate.minusDays(-7),
                hours = 10,
                db = database
        ).execute()


        val transaction = RunPayrollTransaction(
                database = database,
                date = payDate
        )

        transaction.execute()

        val pay = 2 * 15.25

        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2018, 4, 29),
                                payDate
                        ),
                        netPay = pay,
                        grossPay = pay,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testPayCommissionedEmployee() {
        val db = InMemoryDatabase()

        AddCommissionedEmployeeTransaction(
                empId = "1",
                address = "New York",
                name = "Bob",
                commissionRate = 0.4,
                salary = 1000.0,
                database = db
        ).execute()

        // Friday the second week of the year
        val payDate = LocalDate.of(2018, 1, 12)

        val transaction = RunPayrollTransaction(
                database = db,
                date = payDate
        )

        transaction.execute()

        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2017, 12, 30),
                                payDate
                        ),
                        netPay = 1000.0,
                        grossPay = 1000.0,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testPayCommissionedEmployeeWrongDate() {
        val db = InMemoryDatabase()

        AddCommissionedEmployeeTransaction(
                empId = "1",
                address = "New York",
                name = "Bob",
                commissionRate = 0.4,
                salary = 1000.0,
                database = db
        ).execute()

        val payDate = LocalDate.of(2018, 1, 11)

        val transaction = RunPayrollTransaction(
                database = db,
                date = payDate
        )

        transaction.execute()

        assertEquals(
                expected = null,
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testPayCommissionedEmployeeWithSalesReceipt() {
        val db = InMemoryDatabase()

        AddCommissionedEmployeeTransaction(
                empId = "1",
                address = "New York",
                name = "Bob",
                commissionRate = 10.0,
                salary = 1000.0,
                database = db
        ).execute()

        val payDate = LocalDate.of(2018, 1, 12)

        PostSalesReceiptTransaction(
                empId = "1",
                amount = 2.0,
                date = payDate,
                db = db
        ).execute()

        val transaction = RunPayrollTransaction(
                database = db,
                date = payDate
        )

        transaction.execute()

        val pay = 1000 + (2 * 10.0)

        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2017, 12, 30),
                                payDate
                        ),
                        netPay = pay,
                        grossPay = pay,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testPayCommissionedEmployeeWithTwoSalesReceipts() {
        val db = InMemoryDatabase()

        AddCommissionedEmployeeTransaction(
                empId = "1",
                address = "New York",
                name = "Bob",
                commissionRate = 10.0,
                salary = 1000.0,
                database = db
        ).execute()

        val payDate = LocalDate.of(2018, 1, 12)

        PostSalesReceiptTransaction(
                empId = "1",
                amount = 2.0,
                date = payDate,
                db = db
        ).execute()

        PostSalesReceiptTransaction(
                empId = "1",
                amount = 6.0,
                date = payDate.minusDays(2),
                db = db
        ).execute()

        val transaction = RunPayrollTransaction(
                database = db,
                date = payDate
        )

        transaction.execute()

        val pay = 1000 + (2 * 10.0) + (6 * 10.0)

        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2017, 12, 30),
                                payDate
                        ),
                        netPay = pay,
                        grossPay = pay,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testPayCommissionedEmployeeWithTwoSalesReceiptsSpanningTwoPeriods() {
        val db = InMemoryDatabase()

        AddCommissionedEmployeeTransaction(
                empId = "1",
                address = "New York",
                name = "Bob",
                commissionRate = 10.0,
                salary = 1000.0,
                database = db
        ).execute()

        val payDate = LocalDate.of(2018, 1, 12)

        PostSalesReceiptTransaction(
                empId = "1",
                amount = 2.0,
                date = payDate,
                db = db
        ).execute()

        PostSalesReceiptTransaction(
                empId = "1",
                amount = 6.0,
                date = payDate.minusDays(15),
                db = db
        ).execute()

        val transaction = RunPayrollTransaction(
                database = db,
                date = payDate
        )

        transaction.execute()

        val pay = 1000 + (2 * 10.0)

        assertEquals(
                expected = PayCheck(
                        deductions = 0.0,
                        payInterval = Pair(
                                LocalDate.of(2017, 12, 30),
                                payDate
                        ),
                        netPay = pay,
                        grossPay = pay,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testMembershipNoServiceCharges() {
        val database = InMemoryDatabase()

        AddSalariedEmployeeTransaction(
                empId = "1",
                salary = 1000.0,
                name = "Bob",
                address = "New York",
                database = database
        ).execute()

        PutEmployeeInUnionTransaction(
                empId = "1",
                dues = 10.0,
                memberId = "1",
                db = database
        ).execute()

        val startPayInterval = LocalDate.of(2011, 12, 1)
        val payDate = LocalDate.of(2011, 12, 31)

        val transaction = RunPayrollTransaction(
                date = payDate,
                database = database
        )

        transaction.execute()

        val payGross = 1000.0
        val deductions = 5 * 10.0
        val payNet = payGross - deductions

        assertEquals(
                expected = PayCheck(
                        deductions = deductions,
                        payInterval = Pair(startPayInterval, payDate),
                        netPay = payNet,
                        grossPay = payGross,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testMemberShipOneServiceCharge() {
        val database = InMemoryDatabase()



        AddSalariedEmployeeTransaction(
                empId = "1",
                salary = 1000.0,
                name = "Bob",
                address = "New York",
                database = database
        ).execute()

        PutEmployeeInUnionTransaction(
                empId = "1",
                dues = 10.0,
                memberId = "1",
                db = database
        ).execute()

        PostUnionServiceChargeTransaction(
                db = database,
                amount = 200.0,
                memberId = "1",
                date = LocalDate.of(2011, 12, 4)
        ).execute()

        val startPayInterval = LocalDate.of(2011, 12, 1)
        val payDate = LocalDate.of(2011, 12, 31)

        val transaction = RunPayrollTransaction(
                date = payDate,
                database = database
        )

        transaction.execute()

        val payGross = 1000.0
        val deductions = 5 * 10.0 + 200.0
        val payNet = payGross - deductions

        assertEquals(
                expected = PayCheck(
                        deductions = deductions,
                        payInterval = Pair(startPayInterval, payDate),
                        netPay = payNet,
                        grossPay = payGross,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

    @Test
    fun testMemberShipTwoServiceChargesSpanningTwoTimePeriods() {
        val database = InMemoryDatabase()

        AddSalariedEmployeeTransaction(
                empId = "1",
                salary = 1000.0,
                name = "Bob",
                address = "New York",
                database = database
        ).execute()

        PutEmployeeInUnionTransaction(
                empId = "1",
                dues = 10.0,
                memberId = "1",
                db = database
        ).execute()

        PostUnionServiceChargeTransaction(
                db = database,
                amount = 200.0,
                memberId = "1",
                date = LocalDate.of(2011, 12, 4)
        ).execute()

        PostUnionServiceChargeTransaction(
                db = database,
                amount = 400.0,
                memberId = "1",
                date = LocalDate.of(2011, 11, 8)
        ).execute()

        val startPayInterval = LocalDate.of(2011, 12, 1)
        val payDate = LocalDate.of(2011, 12, 31)

        val transaction = RunPayrollTransaction(
                date = payDate,
                database = database
        )

        transaction.execute()

        val payGross = 1000.0
        val deductions = 5 * 10.0 + 200.0
        val payNet = payGross - deductions

        assertEquals(
                expected = PayCheck(
                        deductions = deductions,
                        payInterval = Pair(startPayInterval, payDate),
                        netPay = payNet,
                        grossPay = payGross,
                        paymentMethod = HoldPaycheck
                ),
                actual = transaction.getPaycheck("1")
        )
    }

}