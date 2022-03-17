package info.alaz.krywatcher.service

import info.alaz.krywatcher.db.entity.HttpLink
import info.alaz.krywatcher.db.repository.HttpLinkRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.UUID
import javax.transaction.Transactional

@Service
class HttpLinkService(
    private val httpLinkRepository: HttpLinkRepository
) : CrudService<HttpLink, String> {

    override fun findById(id: String): HttpLink? {
        return httpLinkRepository.findById(id).orElse(null)
    }

    fun findAll(): List<HttpLink> {
        return this.httpLinkRepository.findAll()
    }

    @Transactional
    override fun insert(record: HttpLink): HttpLink {
        record.version = 0
        return this.httpLinkRepository.save(record)
    }

    @Transactional
    override fun update(oldRecord: HttpLink, newRecord: HttpLink): HttpLink {
        oldRecord.name = newRecord.name
        oldRecord.url = newRecord.url
        oldRecord.method = newRecord.method
        oldRecord.successStatus = newRecord.successStatus
        oldRecord.lastModifiedDate = ZonedDateTime.now()

        return this.httpLinkRepository.save(oldRecord);
    }

    fun delete(uuid: String) {
        return this.httpLinkRepository.deleteById(uuid)
    }
}
