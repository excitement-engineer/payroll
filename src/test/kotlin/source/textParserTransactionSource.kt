package source

import org.junit.Test
import payroll.source.TextParserTransactionSource
import payroll.transaction.Transaction
import payroll.transaction.TransactionFactory
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals
import kotlin.test.assertFails


class TextParserTransactionSourceTest {

    @Test
    fun testAddHourlyEmployee() {

        val transaction = "AddEmp 1 \"Bob\" \"New York\" H 5"

        val source = createSource(transaction)

        assertEquals(
                expected = TestTransaction,
                actual = source.getTransaction()
        )
    }

    @Test
    fun testErrorIncorrectStructure() {

        val transaction = "WRONG"

        val source = createSource(transaction)

        val error = assertFails {
            source.getTransaction()
        }

        assertEquals(
                expected = "Incorrect transaction structure",
                actual = error.message
        )
    }

    @Test
    fun testNullIfNoTransaction() {

        val transaction = "\n"

        val source = createSource(transaction)

        assertEquals(
            expected = null,
            actual = source.getTransaction()
        )
    }


    private fun createSource(transaction: String): TextParserTransactionSource {
        val input = ByteArrayInputStream(transaction.toByteArray())

        return TextParserTransactionSource(input, TestTransactionFactory)
    }
}

object TestTransaction: Transaction {
    override fun execute() {}
}

object TestTransactionFactory: TransactionFactory {
    override fun makeAddHourlyEmployeeTransaction(empId: String, name: String, address: String, hourlyRate: Double): Transaction {

        assertEquals("1", empId)
        assertEquals("Bob", name)
        assertEquals(address, "New York")
        assertEquals(5.0, hourlyRate)

        return TestTransaction
    }

}