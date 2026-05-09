package tech.alaz.http.watcher.service

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import tech.alaz.http.watcher.db.entity.HttpLinkEntity
import tech.alaz.http.watcher.db.entity.SupportedHttpMethod
import tech.alaz.http.watcher.db.entity.UserEntity
import tech.alaz.http.watcher.db.repository.HttpLinkRepository
import java.time.ZonedDateTime
import java.util.Optional
import java.util.UUID

internal class HttpLinkEntityServiceTest {

    private val repository: HttpLinkRepository = mockk()
    private val service = HttpLinkService(repository)

    private val user = UserEntity("testuser", 0L, "password")
    private val id = UUID.randomUUID().toString()
    private val entity = HttpLinkEntity(
        id = id, version = 0L, name = "Test Link",
        url = "https://example.com", user = user,
        method = SupportedHttpMethod.GET, successStatus = 200,
        createdDate = ZonedDateTime.now(), lastModifiedDate = ZonedDateTime.now()
    )

    @Test
    fun `findById returns entity when present`() {
        every { repository.findById(id) } returns Optional.of(entity)
        assertThat(service.findById(id)).isEqualTo(entity)
    }

    @Test
    fun `findById returns null when absent`() {
        every { repository.findById(id) } returns Optional.empty()
        assertThat(service.findById(id)).isNull()
    }

    @Test
    fun `findAll returns list from repository`() {
        every { repository.findAll() } returns listOf(entity)
        assertThat(service.findAll()).containsExactly(entity)
    }

    @Test
    fun `insert sets version to zero before saving`() {
        every { repository.save(any()) } returns entity
        service.insert(entity.copy(version = 5L))
        verify { repository.save(withArg { assertThat(it.version).isEqualTo(0L) }) }
    }

    @Test
    fun `update applies new field values before saving`() {
        val updated = entity.copy(
            name = "Updated", url = "https://updated.com",
            method = SupportedHttpMethod.HEAD, successStatus = 201
        )
        every { repository.save(any()) } returns updated
        service.update(entity, updated)
        verify {
            repository.save(withArg {
                assertThat(it.name).isEqualTo("Updated")
                assertThat(it.url).isEqualTo("https://updated.com")
                assertThat(it.method).isEqualTo(SupportedHttpMethod.HEAD)
                assertThat(it.successStatus).isEqualTo(201)
            })
        }
    }

    @Test
    fun `delete calls repository deleteById`() {
        every { repository.deleteById(id) } just runs
        service.delete(id)
        verify { repository.deleteById(id) }
    }
}