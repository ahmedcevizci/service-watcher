package info.alaz.krywatcher.db.repository

import info.alaz.krywatcher.db.entity.HttpLinkStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface HttpLinkStatusRepository : JpaRepository<HttpLinkStatus, String>
