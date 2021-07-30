package party.domain.event

import party.domain.model.UserInOutEvent
import party.domain.model.UserInParty
import party.domain.model.UserLeaveEvent

interface EventNotifier {
    suspend fun notifyUserOut(userInOutEvent: UserInOutEvent)
    suspend fun notifyUserIn(userInOutEvent: UserInOutEvent)
}