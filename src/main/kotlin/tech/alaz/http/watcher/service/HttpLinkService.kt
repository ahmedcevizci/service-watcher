package tech.alaz.http.watcher.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.alaz.http.watcher.db.entity.HttpLinkEntity
import tech.alaz.http.watcher.db.repository.HttpLinkRepository
import java.time.ZonedDateTime

@Service
class HttpLinkService(
    private val httpLinkRepository: HttpLinkRepository
) : CrudService<HttpLinkEntity, String> {

    override fun findById(id: String): HttpLinkEntity? {
        return httpLinkRepository.findById(id).orElse(null)
    }

    fun findAll(): List<HttpLinkEntity> {
        return this.httpLinkRepository.findAll()
    }

    @Transactional
    override fun insert(record: HttpLinkEntity): HttpLinkEntity {
        record.version = 0
        return this.httpLinkRepository.save(record)
    }

    @Transactional
    override fun update(oldRecord: HttpLinkEntity, newRecord: HttpLinkEntity): HttpLinkEntity {
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
