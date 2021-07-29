package party.infra.event

import party.domain.event.EventNotifier
import party.domain.model.UserInOutEvent
import party.domain.model.UserInParty
import party.domain.model.UserLeaveEvent
import javax.inject.Singleton

@Singleton
class EventNotifierMemory : EventNotifier {

    override suspend fun notifyNewUser(userInParty: UserInParty) {
        TODO("Not yet implemented")
    }

    override suspend fun notifyUserLeave(userLeaveEvent: UserLeaveEvent) {
        TODO("Not yet implemented")
    }

    override suspend fun notifyInOutEvent(userInOutEvent: UserInOutEvent) {
        TODO("Not yet implemented")
    }

}