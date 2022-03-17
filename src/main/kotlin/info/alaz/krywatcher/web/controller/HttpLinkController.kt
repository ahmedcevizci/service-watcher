package info.alaz.krywatcher.web.controller

import info.alaz.krywatcher.db.entity.HttpLink
import info.alaz.krywatcher.db.entity.KryUser
import info.alaz.krywatcher.service.HttpLinkService
import info.alaz.krywatcher.service.KryUserService
import info.alaz.krywatcher.web.dto.HttpLinkRequestDto
import info.alaz.krywatcher.web.dto.HttpLinkRequestValidator
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.util.UUID

@RestController
@RequestMapping("/http-links")
class HttpLinkController(
    val httpLinkService: HttpLinkService,
    val kryUserService: KryUserService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping(consumes = ["application/json"])
    fun create(@RequestParam newHttpLink: HttpLinkRequestDto): ResponseEntity<*> {
        val validationError = HttpLinkRequestValidator.validate(newHttpLink)
        if (validationError.isValid()) {
            val user: KryUser = this.kryUserService.findById(newHttpLink.name)
                ?: return ResponseEntity("User cannot be found.", HttpStatus.BAD_REQUEST)
            val webLink = httpLinkService.insert(mapToHttpLink(user, newHttpLink))
            return ResponseEntity(webLink, HttpStatus.OK)
        }
        return ResponseEntity(validationError, HttpStatus.BAD_REQUEST)
    }

    // TODO use MapStruct
    private fun mapToHttpLink(user: KryUser, newHttpLink: HttpLinkRequestDto): HttpLink {
        val now = ZonedDateTime.now()
        return HttpLink(
            UUID.randomUUID().toString(),
            0,
            newHttpLink.name,
            newHttpLink.url,
            user,
            newHttpLink.method,
            newHttpLink.successStatus,
            now,
            now
        )
    }

    @GetMapping(value = ["/{uuid}"], produces = ["application/json"])
    fun read(@PathVariable uuid: UUID): ResponseEntity<HttpLink?> { // TODO map to HttpLinkResponseDTO
        return when (val httpLink = httpLinkService.findById(uuid.toString())) {
            null -> ResponseEntity(null, HttpStatus.NOT_FOUND)
            else -> ResponseEntity(httpLink, HttpStatus.OK)
        }
    }

    @GetMapping(produces = ["application/json"])
    fun readAll(): ResponseEntity<List<HttpLink>> { // TODO map to HttpLinkResponseDTO
        return ResponseEntity(httpLinkService.findAll(), HttpStatus.OK)
            // TODO check isEmpty
    }

    @PutMapping("/{uuid}", consumes = ["application/json"])
    fun update(@PathVariable uuid: UUID, @RequestParam newHttpLink: HttpLinkRequestDto): ResponseEntity<*> {
        val validationError = HttpLinkRequestValidator.validate(newHttpLink)
        if (validationError.isValid()) {
            val user: KryUser = this.kryUserService.findById(newHttpLink.name)
                ?: return ResponseEntity("User cannot be found.", HttpStatus.BAD_REQUEST)
            val webLink = httpLinkService.upsert(mapToHttpLink(user, newHttpLink))  // TODO use another mapper
            return ResponseEntity(webLink, HttpStatus.OK)
        }
        return ResponseEntity(validationError, HttpStatus.BAD_REQUEST)
    }


    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<*> {
        return when (httpLinkService.findById(uuid.toString())) {
            null -> ResponseEntity(null, HttpStatus.NOT_FOUND)
            else -> {
                httpLinkService.delete(uuid.toString())
                // TODO delete HttpLinkStatus as well or use DELETE CASCADE
                ResponseEntity(null, HttpStatus.OK)
            }
        }
    }

}
