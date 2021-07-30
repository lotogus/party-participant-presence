package party.infra.repository

import party.domain.model.Party
import party.domain.model.User
import party.domain.model.UserInParty
import party.domain.model.UserInPartyActivity
import party.domain.repository.PartyRepository
import party.domain.repository.UserInPartyActivityRepository
import party.domain.repository.UserInPartyRepository
import party.domain.repository.UserRepository
import java.time.LocalDateTime
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class PartyRepositoryMemory : PartyRepository {
    private val map = mutableMapOf<String, Party>()

    override suspend fun save(party: Party): Party {
        map[party.id] = party
        return party
    }

    override suspend fun findById(id: String): Party? {
        return map[id]
    }
}

@Singleton
class UserRepositoryMemory : UserRepository {
    private val map = mutableMapOf<String, User>()

    override suspend fun save(user: User): User {
        map[user.id] = user
        return user
    }

    override suspend fun findById(id: String): User? {
        return map[id]
    }
}

@Singleton
class UserInPartyRepositoryMemory : UserInPartyRepository {
    private val map = mutableMapOf<String, UserInParty>()

    override suspend fun save(userInParty: UserInParty): UserInParty {
        val userInPartyToSave =
            if (userInParty.id == null)
                userInParty.copy(id = Random(100).nextInt().toString())
            else userInParty
        map[userInPartyToSave.id!!] = userInPartyToSave
        return userInPartyToSave
    }

    override suspend fun findByPartyIdAndUserId(partyId: String, userId: String): UserInParty? {
        return map.values.firstOrNull { it.party.id == partyId && it.user.id == userId }
    }

    override suspend fun findByPartyId(partyId: String): List<UserInParty> {
        return map.values.filter { it.party.id == partyId }
    }

    override suspend fun deleteById(id: String) {
        map.remove(id)
    }

}

@Singleton
class UserInPartyActivityRepositoryMemory : UserInPartyActivityRepository {
    private val map = mutableMapOf<String, UserInPartyActivity>()

    override suspend fun save(userInPartyActivity: UserInPartyActivity): UserInPartyActivity {
        val userInPartyActivityToSave =
            if (userInPartyActivity.id == null)
                userInPartyActivity.copy(id = Random(100).nextInt().toString())
            else userInPartyActivity
        map[userInPartyActivityToSave.id!!] = userInPartyActivityToSave
        return userInPartyActivityToSave
    }

    override suspend fun findByTimeGreaterThan(dateFrom: LocalDateTime): List<UserInPartyActivity> {
        return map.values.filter { it.time > dateFrom }
    }

    override suspend fun deleteById(id: String) {
        map.remove(id)
    }

    override suspend fun findByPartyIdAndUserId(partyId: String, userId: String): UserInPartyActivity? {
        return map.values.firstOrNull { it.partyId==partyId && it.userId==userId }
    }
}