package party.infra

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import party.JoinPartyRequest
import party.ListenInOutEventRequest
import party.domain.model.Party
import party.domain.model.User
import party.domain.repository.PartyRepository
import party.domain.repository.UserRepository
import party.infra.api.PresenceApiEndpoint
import javax.inject.Inject

@MicronautTest(packages = ["party"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JoinPartyIntegrationTest {

    @Inject
    lateinit var presenceApiEndpoint: PresenceApiEndpoint

    @Inject
    lateinit var partyRepository: PartyRepository

    @Inject
    lateinit var userRepository: UserRepository

    @BeforeAll
    fun before(): Unit = runBlocking {
        partyRepository.save(Party("1", "test 1"))
        userRepository.save(User("700", "test 1"))
    }

    @Test
    fun `given an empty users party when an user join should return only one user as response`(): Unit = runBlocking {

        val users = presenceApiEndpoint
            .joinParty(JoinPartyRequest.newBuilder().setPartyId("1").setUserId("700").build())
            .usersList

        assertThat(users).hasSize(1)

        //val flow = presenceApiEndpoint.listenInOutEvent(ListenInOutEventRequest.newBuilder().setPartyId("01").build())
        //val c = flow.count()
        //assertThat(c).isEqualTo(1)
    }
}
