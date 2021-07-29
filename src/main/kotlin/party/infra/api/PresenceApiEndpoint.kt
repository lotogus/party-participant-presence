package party.infra.api

import io.grpc.Status
import io.grpc.StatusException
import party.*
import party.domain.action.JoinPartyAction
import party.domain.action.LeavePartyAction
import party.domain.action.PingAction
import party.domain.model.NotFoundError
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class PresenceApiEndpoint(
    private val joinPartyAction: JoinPartyAction,
    private val leavePartyAction: LeavePartyAction,
    private val pingAction: PingAction
) : PresenceApiServiceGrpcKt.PresenceApiServiceCoroutineImplBase() {

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
}