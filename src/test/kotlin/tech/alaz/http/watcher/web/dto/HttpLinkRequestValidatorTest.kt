package tech.alaz.http.watcher.web.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HttpLinkRequestValidatorTest {

    private val validRequest = HttpLinkRequestDto(name = "testuser", url = "https://example.com", user = "testuser")

    @Test
    fun `valid request passes validation`() {
        assertThat(HttpLinkRequestValidator.validate(validRequest).isValid()).isTrue()
    }

    @Test
    fun `null request fails validation`() {
        assertThat(HttpLinkRequestValidator.validate(null).isValid()).isFalse()
    }

    @Test
    fun `blank name fails validation`() {
        assertThat(HttpLinkRequestValidator.validate(validRequest.copy(name = "")).isValid()).isFalse()
    }

    @Test
    fun `blank url fails validation`() {
        assertThat(HttpLinkRequestValidator.validate(validRequest.copy(url = "")).isValid()).isFalse()
    }

    @Test
    fun `invalid url fails validation`() {
        assertThat(HttpLinkRequestValidator.validate(validRequest.copy(url = "not-a-url")).isValid()).isFalse()
    }

    @Test
    fun `success status below 100 fails validation`() {
        assertThat(HttpLinkRequestValidator.validate(validRequest.copy(successStatus = 99)).isValid()).isFalse()
    }

    @Test
    fun `success status 600 fails validation`() {
        assertThat(HttpLinkRequestValidator.validate(validRequest.copy(successStatus = 600)).isValid()).isFalse()
    }

    @Test
    fun `success status boundary 100 passes validation`() {
        assertThat(HttpLinkRequestValidator.validate(validRequest.copy(successStatus = 100)).isValid()).isTrue()
    }

    @Test
    fun `success status boundary 599 passes validation`() {
        assertThat(HttpLinkRequestValidator.validate(validRequest.copy(successStatus = 599)).isValid()).isTrue()
    }
}