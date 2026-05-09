package tech.alaz.http.watcher.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.alaz.http.watcher.db.entity.HttpLinkEntity

@Repository
interface HttpLinkRepository : JpaRepository<HttpLinkEntity, String>
