package party.infra.api

import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import party.*
import party.domain.action.JoinPartyAction
import party.domain.action.LeavePartyAction
import party.domain.action.PingAction
import party.domain.model.NotFoundError
import party.domain.model.UserInOutEvent
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class PresenceApiEndpoint(
    private val joinPartyAction: JoinPartyAction,
    private val leavePartyAction: LeavePartyAction,
    private val pingAction: PingAction,
    private val eventChannel: Channel<UserInOutEvent>
) : PresenceApiServiceGrpcKt.PresenceApiServiceCoroutineImplBase() {

    private val listeners = mutableMapOf<String, MutableList<ProducerScope<ListenInOutEventReply>>>()

    override suspend fun joinParty(request: JoinPartyRequest): JoinPartyReply {
        try {
            val users = joinPartyAction.joinParty(request.partyId, request.userId)
                .map { User.newBuilder().setId(it.id).setName(it.name).build() }
            return JoinPartyReply.newBuilder()
                .addAllUsers(users)
                .build()
        } catch (ex: NotFoundError) {
            throw StatusException(Status.NOT_FOUND)
        }
    }

    override suspend fun leaveParty(request: LeavePartyRequest): LeavePartyReply {
        leavePartyAction.leaveParty(request.partyId, request.userId)
        return LeavePartyReply.newBuilder().build()
    }

    override suspend fun ping(request: PingRequest): PingReply {
        pingAction.ping(request.partyId, request.userId)
        return PingReply.newBuilder().build()
    }

    fun notifyListeners(userInOutEvent: UserInOutEvent) {
        val allScores = ListenInOutEventReply.newBuilder()
            .setPartyId(userInOutEvent.party.id)
            .setUserId(userInOutEvent.user.id)
            .setType(userInOutEvent.type.name)
            .build()

        listeners[userInOutEvent.party.id]?.forEach {
            GlobalScope.launch { it.send(allScores) }
        }
    }

    override fun listenInOutEvent(request: ListenInOutEventRequest): Flow<ListenInOutEventReply> = channelFlow {
        val partyId = request.partyId
        if (listeners[partyId] == null) {
            listeners[partyId] = mutableListOf(this)
        } else {
            listeners[partyId]?.add(this)
        }
        //notifyListeners(this)
        awaitClose { }

        //return eventChannel.consumeAsFlow()
          //  .map { ListenInOutEventReply.newBuilder().setPartyId(it.party.id).setUserId(it.user.id).build() }
    }

}