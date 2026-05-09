package tech.alaz.http.watcher.web.controller

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import tech.alaz.http.watcher.db.entity.HttpLinkEntity
import tech.alaz.http.watcher.db.entity.SupportedHttpMethod
import tech.alaz.http.watcher.db.entity.UserEntity
import tech.alaz.http.watcher.service.HttpLinkService
import tech.alaz.http.watcher.service.UserService
import tech.alaz.http.watcher.web.dto.HttpLinkRequestDto
import java.time.ZonedDateTime
import java.util.UUID

internal class HttpLinkEntityControllerTest {

    private val httpLinkService: HttpLinkService = mockk()
    private val userService: UserService = mockk()
    private val controller = HttpLinkController(httpLinkService, userService)

    private val user = UserEntity("testuser", 0L, "password")
    private val linkUuid = UUID.randomUUID()
    private val linkId = linkUuid.toString()
    private val httpLink = HttpLinkEntity(
        id = linkId, version = 0L, name = "testuser",
        url = "https://example.com", user = user,
        method = SupportedHttpMethod.GET, successStatus = 200,
        createdDate = ZonedDateTime.now(), lastModifiedDate = ZonedDateTime.now()
    )
    private val validRequest = HttpLinkRequestDto(name = "testuser", url = "https://example.com", user = "testuser")

    @Test
    fun `create returns 200 when request is valid and user exists`() {
        every { userService.findById("testuser") } returns user
        every { httpLinkService.insert(any()) } returns httpLink

        val result = controller.create(validRequest)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(httpLink)
    }

    @Test
    fun `create returns 400 when name is blank`() {
        val result = controller.create(validRequest.copy(name = ""))
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `create returns 400 when url is invalid`() {
        val result = controller.create(validRequest.copy(url = "not-a-url"))
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `create returns 400 when user not found`() {
        every { userService.findById("testuser") } returns null

        val result = controller.create(validRequest)

        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `read returns 200 with body when link exists`() {
        every { httpLinkService.findById(linkId) } returns httpLink

        val result = controller.read(linkUuid)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(httpLink)
    }

    @Test
    fun `read returns 404 when link not found`() {
        every { httpLinkService.findById(any()) } returns null

        val result = controller.read(linkUuid)

        assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `readAll returns 200 with all links`() {
        every { httpLinkService.findAll() } returns listOf(httpLink)

        val result = controller.readAll()

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).containsExactly(httpLink)
    }

    @Test
    fun `update returns 200 when request is valid and user exists`() {
        every { userService.findById("testuser") } returns user
        every { httpLinkService.upsert(any()) } returns httpLink

        val result = controller.update(linkUuid, validRequest)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `update returns 400 when url is invalid`() {
        val result = controller.update(linkUuid, validRequest.copy(url = "not-a-url"))
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `delete returns 200 and calls service when link exists`() {
        every { httpLinkService.findById(linkId) } returns httpLink
        every { httpLinkService.delete(linkId) } just runs

        val result = controller.delete(linkUuid)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        verify { httpLinkService.delete(linkId) }
    }

    @Test
    fun `delete returns 404 when link not found`() {
        every { httpLinkService.findById(any()) } returns null

        val result = controller.delete(linkUuid)

        assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}
