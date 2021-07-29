package party.infra.repository

import party.domain.model.Party
import party.domain.model.User
import party.domain.model.UserInParty
import party.domain.model.UserInPartyActivity
import party.domain.repository.PartyRepository
import party.domain.repository.UserInPartyActivityRepository
import party.domain.repository.UserInPartyRepository
import party.domain.repository.UserRepository
import javax.inject.Singleton

@Singleton
class PartyRepositoryMemory : PartyRepository {
    override suspend fun findById(id: String): Party? {
        TODO("Not yet implemented")
    }
}

@Singleton
class UserRepositoryMemory : UserRepository {
    override suspend fun findById(id: String): User? {
        TODO("Not yet implemented")
    }
}

@Singleton
class UserInPartyRepositoryMemory : UserInPartyRepository {
    override suspend fun save(userInParty: UserInParty): UserInParty {
        TODO("Not yet implemented")
    }

    override suspend fun findByPartyIdAndUserId(partyId: String, userId: String): UserInParty? {
        TODO("Not yet implemented")
    }

    override suspend fun findByPartyId(partyId: String): List<UserInParty> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: String) {
        TODO("Not yet implemented")
    }

}

@Singleton
class UserInPartyActivityRepositoryMemory : UserInPartyActivityRepository {
    override suspend fun save(userInPartyActivity: UserInPartyActivity): UserInPartyActivity {
        TODO("Not yet implemented")
    }

}