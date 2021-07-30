package party.domain.action

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import party.domain.event.EventNotifier
import party.domain.model.Party
import party.domain.model.User
import party.domain.model.UserInParty
import party.domain.repository.PartyRepository
import party.domain.repository.UserInPartyRepository
import party.domain.repository.UserRepository

class JoinPartyActionTest {

    @Test
    fun `for user joining a party, should save userInParty, send notification & return the users in party`() {
        //given
        val partyId = "100"
        val userId = "70000"
        val partyRepository = mockk<PartyRepository>()
        val userRepository = mockk<UserRepository>()
        val eventNotifier = mockk<EventNotifier>()
        val userInPartyRepository = mockk<UserInPartyRepository>()
        val userInPartySlot = slot<UserInParty>()

        //when
        coEvery { partyRepository.findById(partyId) } returns Party(partyId, "1")
        coEvery { userRepository.findById(userId) } returns User(userId, "john")
        coEvery { userInPartyRepository.save(capture(userInPartySlot)) } answers { userInPartySlot.captured }
        coEvery { eventNotifier.notifyUserIn(any()) } just Runs
        coEvery { userInPartyRepository.findByPartyId(partyId) } returns listOf(
            UserInParty(
                "44",
                User(userId, "john"),
                Party(partyId, "1")
            )
        )

        val joinPartyAction = JoinPartyAction(partyRepository, userRepository, eventNotifier, userInPartyRepository)

        val joinParty = runBlocking { joinPartyAction.joinParty(partyId, userId) }

        //then
        assertThat(joinParty).hasSize(1)
    }

}