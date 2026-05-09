package tech.alaz.http.watcher.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.alaz.http.watcher.db.entity.HttpLinkEntity
import tech.alaz.http.watcher.db.entity.HttpLinkStatusEntity
import tech.alaz.http.watcher.db.entity.Status
import tech.alaz.http.watcher.db.repository.HttpLinkStatusRepository

@Service
class HttpLinkStatusService(
    private val httpLinkStatusRepository: HttpLinkStatusRepository
) : CrudService<HttpLinkStatusEntity, String> {

    override fun findById(id: String): HttpLinkStatusEntity? {
        return httpLinkStatusRepository.findById(id).orElse(null)
    }

    @Transactional
    override fun insert(record: HttpLinkStatusEntity): HttpLinkStatusEntity {
        record.version = 0
        return this.httpLinkStatusRepository.saveAndFlush(record)
    }

    @Transactional
    override fun update(oldRecord: HttpLinkStatusEntity, newRecord: HttpLinkStatusEntity): HttpLinkStatusEntity {
        oldRecord.lastStatus = newRecord.lastStatus
        if (newRecord.lastStatus == Status.OK) {
            oldRecord.lastOkDate = newRecord.lastOkDate
        } else {
            oldRecord.lastFailDate = newRecord.lastFailDate
        }
        return httpLinkStatusRepository.saveAndFlush(oldRecord)
    }

    @Transactional
    fun updateStatusOfHttpLink(successful: Boolean, httpLinkEntity: HttpLinkEntity): HttpLinkStatusEntity {
        val newHttpLinkStatusEntity = if (successful) {
            HttpLinkStatusEntity(httpLinkEntity.id, 0, Status.OK)
        } else {
            HttpLinkStatusEntity(
                httpLinkEntity.id,
                0,
                Status.FAIL
            )
        }

        return upsert(newHttpLinkStatusEntity)
    }

    fun findAll(): List<HttpLinkStatusEntity> {
        return this.httpLinkStatusRepository.findAll()
    }
}
