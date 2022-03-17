package info.alaz.krywatcher.service

import info.alaz.krywatcher.db.entity.KryUser
import info.alaz.krywatcher.db.repository.KryUserRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class KryUserService(
    private val kryUserRepository: KryUserRepository
) : CrudService<KryUser, String> {

    override fun findById(id: String): KryUser? {
        return kryUserRepository.findById(id).orElse(null)
    }

    @Transactional
    override fun insert(record: KryUser): KryUser {
        record.version = null
        return this.kryUserRepository.save(record)
    }

    @Transactional
    override fun update(oldRecord: KryUser, newRecord: KryUser): KryUser {
        oldRecord.password = newRecord.password
        oldRecord.lastModifiedDate = ZonedDateTime.now()

        return this.kryUserRepository.save(oldRecord);
    }
}
