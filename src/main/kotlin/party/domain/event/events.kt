package party.domain.event

import party.domain.model.UserInOutEvent
import party.domain.model.UserInParty
import party.domain.model.UserLeaveEvent

interface EventNotifier {
    suspend fun notifyNewUser(userInParty: UserInParty)
    suspend fun notifyUserLeave(userLeaveEvent: UserLeaveEvent)
    suspend fun notifyInOutEvent(userInOutEvent: UserInOutEvent)
}