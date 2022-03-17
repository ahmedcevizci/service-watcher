package info.alaz.krywatcher.db.repository

import info.alaz.krywatcher.db.entity.KryUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KryUserRepository : JpaRepository<KryUser, String>
