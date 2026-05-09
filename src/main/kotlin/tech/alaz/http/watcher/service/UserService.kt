package tech.alaz.http.watcher.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.alaz.http.watcher.db.entity.UserEntity
import tech.alaz.http.watcher.db.repository.UserRepository
import java.time.ZonedDateTime

@Service
class UserService(
    private val userRepository: UserRepository
) : CrudService<UserEntity, String> {

    override fun findById(id: String): UserEntity? {
        return userRepository.findById(id).orElse(null)
    }

    @Transactional
    override fun insert(record: UserEntity): UserEntity {
        record.version = null
        return this.userRepository.save(record)
    }

    @Transactional
    override fun update(oldRecord: UserEntity, newRecord: UserEntity): UserEntity {
        oldRecord.password = newRecord.password
        oldRecord.lastModifiedDate = ZonedDateTime.now()

        return this.userRepository.save(oldRecord);
    }
}
