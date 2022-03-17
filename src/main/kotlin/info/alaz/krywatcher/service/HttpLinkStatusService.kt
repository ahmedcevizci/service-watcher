package info.alaz.krywatcher.service

import info.alaz.krywatcher.db.entity.HttpLink
import info.alaz.krywatcher.db.entity.HttpLinkStatus
import info.alaz.krywatcher.db.entity.Status
import info.alaz.krywatcher.db.repository.HttpLinkStatusRepository
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
class HttpLinkStatusService(
    private val httpLinkStatusRepository: HttpLinkStatusRepository
) : CrudService<HttpLinkStatus, String> {

    override fun findById(id: String): HttpLinkStatus? {
        return httpLinkStatusRepository.findById(id).orElse(null)
    }

    @Transactional
    override fun insert(record: HttpLinkStatus): HttpLinkStatus {
        record.version = 0
        return this.httpLinkStatusRepository.saveAndFlush(record)
    }

    @Transactional
    override fun update(oldRecord: HttpLinkStatus, newRecord: HttpLinkStatus): HttpLinkStatus {
        oldRecord.lastStatus = newRecord.lastStatus
        if (newRecord.lastStatus == Status.OK) {
            oldRecord.lastOkDate = newRecord.lastOkDate
        } else {
            oldRecord.lastFailDate = newRecord.lastFailDate
        }
        return httpLinkStatusRepository.saveAndFlush(oldRecord)
    }

    @Transactional
    fun updateStatusOfHttpLink(successful: Boolean, httpLink: HttpLink): HttpLinkStatus {
        val newHttpLinkStatus = if (successful) {
            HttpLinkStatus(httpLink.id, 0, Status.OK)
        } else {
            HttpLinkStatus(httpLink.id, 0, Status.FAIL)
        }

        return upsert(newHttpLinkStatus)
    }

    fun findAll(): List<HttpLinkStatus> {
        return this.httpLinkStatusRepository.findAll()
    }
}
