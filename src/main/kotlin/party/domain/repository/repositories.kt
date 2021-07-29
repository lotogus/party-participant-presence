package party.domain.repository

import party.domain.model.Party
import party.domain.model.User
import party.domain.model.UserInParty
import party.domain.model.UserInPartyActivity

interface PartyRepository {
    suspend fun findById(id:String): Party?
}
interface UserRepository {
    suspend fun findById(id:String): User?
}

interface UserInPartyRepository {
    suspend fun save(userInParty: UserInParty): UserInParty
    suspend fun findByPartyIdAndUserId(partyId: String, userId: String): UserInParty?
    suspend fun findByPartyId(partyId: String): List<UserInParty>
    suspend fun deleteById(id: String)
}

interface UserInPartyActivityRepository {
    suspend fun save(userInPartyActivity: UserInPartyActivity):UserInPartyActivity
}