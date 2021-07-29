package party.domain.action

import party.domain.event.EventNotifier
import party.domain.model.*
import party.domain.repository.PartyRepository
import party.domain.repository.UserInPartyActivityRepository
import party.domain.repository.UserInPartyRepository
import party.domain.repository.UserRepository
import java.time.LocalDateTime
import javax.inject.Singleton

@Singleton
class JoinPartyAction(
    private val partyRepository: PartyRepository,
    private val userRepository: UserRepository,
    private val eventNotifier: EventNotifier,
    private val userInPartyRepository: UserInPartyRepository
) {

    suspend fun joinParty(partyId: String, userId: String): List<User> {
        val party = partyRepository.findById(partyId) ?: throw NotFoundError("partyId $partyId not found")
        val user = userRepository.findById(userId) ?: throw NotFoundError("userId $userId not found")
        val userInParty = trackUserJoinToParty(party, user)
        eventNotifier.notifyNewUser(userInParty)
        return userInPartyRepository.findByPartyId(party.id).map { it.user }
    }

    suspend fun trackUserJoinToParty(party: Party, user: User): UserInParty {
        return userInPartyRepository.save(UserInParty(party = party, user = user))
    }

}

@Singleton
class LeavePartyAction(
    private val partyRepository: PartyRepository,
    private val userRepository: UserRepository,
    private val eventNotifier: EventNotifier,
    private val userInPartyRepository: UserInPartyRepository
) {

    suspend fun leaveParty(partyId: String, userId: String) {
        val party = partyRepository.findById(partyId) ?: throw NotFoundError("partyId $partyId not found")
        val user = userRepository.findById(userId) ?: throw NotFoundError("userId $userId not found")
        eventNotifier.notifyUserLeave(UserLeaveEvent(user, party))
    }

    suspend fun trackUserLeaveToParty(userLeaveEvent: UserLeaveEvent): UserInParty? {
        val userInParty: UserInParty? =
            userInPartyRepository.findByPartyIdAndUserId(userLeaveEvent.party.id, userLeaveEvent.user.id)
        userInParty?.let {
            if (it.id != null) {
                userInPartyRepository.deleteById(it.id)
            }
        }
        return userInParty
    }
}

@Singleton
class InOutEventAction(private val eventNotifier: EventNotifier) {
    suspend fun notifyInOutEvent(userInOutEvent: UserInOutEvent) {
        eventNotifier.notifyInOutEvent(userInOutEvent)
    }
}

@Singleton
class PingAction(
    private val userInPartyActivityRepository: UserInPartyActivityRepository,
    private val leavePartyAction: LeavePartyAction,
) {
    suspend fun ping(partyId: String, userId: String) {
        userInPartyActivityRepository.save(UserInPartyActivity(partyId, userId, LocalDateTime.now()))
    }

    suspend fun processImplicitUserPartyTimeout(partyId: String, userId: String) {
        leavePartyAction.leaveParty(partyId, userId)
    }
}
