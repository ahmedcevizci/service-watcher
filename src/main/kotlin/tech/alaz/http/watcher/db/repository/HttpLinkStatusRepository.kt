package tech.alaz.http.watcher.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.alaz.http.watcher.db.entity.HttpLinkStatusEntity

@Repository
interface HttpLinkStatusRepository : JpaRepository<HttpLinkStatusEntity, String>
