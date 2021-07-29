package party.domain.model

import java.time.LocalDateTime

data class User(val id: String, val name: String)
data class Party(val id: String, val name: String)
data class UserInParty(val id: String?=null, val user: User, val party: Party)

data class UserInPartyActivity(val partyId: String, val userId: String, val time:LocalDateTime)

data class UserLeaveEvent(val user: User, val party: Party)
data class UserInOutEvent(val user: User, val party: Party, val type:InOutType)
enum class InOutType {
    IN, OUT
}
class NotFoundError(override val message: String): Exception(message)