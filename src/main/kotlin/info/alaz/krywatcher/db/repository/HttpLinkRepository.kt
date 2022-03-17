package info.alaz.krywatcher.db.repository

import info.alaz.krywatcher.db.entity.HttpLink
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface HttpLinkRepository : JpaRepository<HttpLink, String>
