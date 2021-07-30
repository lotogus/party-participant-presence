package party.infra.event

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.scheduling.annotation.Scheduled
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.runBlocking
import party.domain.action.JoinPartyAction
import party.domain.action.LeavePartyAction
import party.domain.action.PingAction
import party.domain.event.EventNotifier
import party.domain.model.UserInOutEvent
import party.domain.model.UserLeaveEvent
import party.domain.repository.UserInPartyActivityRepository
import party.infra.api.PresenceApiEndpoint
import java.time.LocalDateTime
import javax.inject.Singleton

@Singleton
class ExpireUsersSchedule(private val pingAction: PingAction,
                          private val userInPartyActivityRepository: UserInPartyActivityRepository) {

    @Scheduled(fixedRate = "30s", initialDelay = "1m")
    fun expireUsers() {
        runBlocking {
            userInPartyActivityRepository.findByTimeGreaterThan(LocalDateTime.now().minusMinutes(2))
                .map {
                    pingAction.processImplicitUserPartyTimeout(it.partyId, it.userId)
                    it
                }
                .map { userInPartyActivityRepository.deleteById(it.id!!) }
        }
    }
}

@Factory
class EventChannelFactory {
    @Bean
    fun eventChannel() = Channel<UserInOutEvent>()
}

@Singleton
class EventListener(
    private val leavePartyAction: LeavePartyAction,
    private val joinPartyAction: JoinPartyAction,
    private val presenceApiEndpoint: PresenceApiEndpoint,
    private val eventChannel: Channel<UserInOutEvent>
) {

    @KafkaListener(groupId = "out-group")
    @Topic("user-out")
    suspend fun listenUserLeave(userLeaveEvent: UserLeaveEvent) {
        leavePartyAction.trackUserLeaveToParty(userLeaveEvent)
    }

    @KafkaListener(groupId = "in-out-group")
    @Topic(*["user-in", "user-out"])
    suspend fun listenInOutEvent(userInOutEvent: UserInOutEvent) {
        //to API
        presenceApiEndpoint.notifyListeners(userInOutEvent)
        //eventChannel.send(userInOutEvent)
        //eventChannel.broadcast().send(userInOutEvent)
    }
}

@KafkaClient
interface EventNotifierKafka : EventNotifier {
    @Topic("user-out")
    override suspend fun notifyUserOut(userInOutEvent: UserInOutEvent)

    @Topic("user-in")
    override suspend fun notifyUserIn(userInOutEvent: UserInOutEvent)
}