package payroll.transaction

import payroll.Database
import payroll.models.UnionServiceCharge
import java.time.LocalDate

class PostUnionServiceChargeTransaction(
    private val memberId: String,
    private val date: LocalDate,
    private val amount: Double,
    private val db: Database
): Transaction {
    override fun execute() {

        val member = db.getUnionMember(memberId)

        if (member == null) {
            throw Exception("No union member for id $memberId")
        }

        val membership = member.unionMembership

        if (membership != null) {
            val serviceCharge = UnionServiceCharge(
                    amount = amount,
                    date = date
            )

            membership.serviceCharges.add(serviceCharge)
        } else {
            throw Exception("Union member with id $memberId has no union membership")
        }

    }
}