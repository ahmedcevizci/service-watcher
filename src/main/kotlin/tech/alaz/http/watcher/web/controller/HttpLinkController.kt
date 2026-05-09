package tech.alaz.http.watcher.web.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.alaz.http.watcher.db.entity.HttpLinkEntity
import tech.alaz.http.watcher.db.entity.UserEntity
import tech.alaz.http.watcher.service.HttpLinkService
import tech.alaz.http.watcher.service.UserService
import tech.alaz.http.watcher.web.dto.HttpLinkRequestDto
import tech.alaz.http.watcher.web.dto.HttpLinkRequestValidator
import java.time.ZonedDateTime
import java.util.*

@RestController
@RequestMapping("/http-links")
class HttpLinkController(
    val httpLinkService: HttpLinkService,
    val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping(consumes = ["application/json"])
    fun create(@RequestBody newHttpLink: HttpLinkRequestDto): ResponseEntity<*> {
        val validationError = HttpLinkRequestValidator.validate(newHttpLink)
        if (validationError.isValid()) {
            val user: UserEntity = this.userService.findById(newHttpLink.name)
                ?: return ResponseEntity("User cannot be found.", HttpStatus.BAD_REQUEST)
            val webLink = httpLinkService.insert(mapToHttpLink(user, newHttpLink))
            return ResponseEntity(webLink, HttpStatus.OK)
        }
        return ResponseEntity(validationError, HttpStatus.BAD_REQUEST)
    }

    // TODO use MapStruct
    private fun mapToHttpLink(user: UserEntity, newHttpLink: HttpLinkRequestDto): HttpLinkEntity {
        val now = ZonedDateTime.now()
        return HttpLinkEntity(
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
    fun read(@PathVariable uuid: UUID): ResponseEntity<*> { // TODO map to HttpLinkResponseDTO
        return when (val httpLink = httpLinkService.findById(uuid.toString())) {
            null -> ResponseEntity<Void>(HttpStatus.NOT_FOUND)
            else -> ResponseEntity(httpLink, HttpStatus.OK)
        }
    }

    @GetMapping(produces = ["application/json"])
    fun readAll(): ResponseEntity<List<HttpLinkEntity>> { // TODO map to HttpLinkResponseDTO
        return ResponseEntity(httpLinkService.findAll(), HttpStatus.OK)
        // TODO check isEmpty
    }

    @PutMapping("/{uuid}", consumes = ["application/json"])
    fun update(@PathVariable uuid: UUID, @RequestBody newHttpLink: HttpLinkRequestDto): ResponseEntity<*> {
        val validationError = HttpLinkRequestValidator.validate(newHttpLink)
        if (validationError.isValid()) {
            val user: UserEntity = this.userService.findById(newHttpLink.name)
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
