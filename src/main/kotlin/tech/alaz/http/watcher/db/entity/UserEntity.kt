package tech.alaz.http.watcher.db.entity

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.ZonedDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tb_user")
data class UserEntity(
    @Id @Column(name = "name", nullable = false) val name: String,
    @Version @Column(name = "version", nullable = false) var version: Long?,
    @Column(name = "password", nullable = false) var password: String,
    @CreatedDate @Column(name = "created_date", nullable = false)
    var createdDate: ZonedDateTime = ZonedDateTime.now(),
    @LastModifiedDate @Column(name = "last_modified_date", nullable = false)
    var lastModifiedDate: ZonedDateTime = ZonedDateTime.now()
) : Identifiable<String> {

    override fun getIdentity(): String {
        return name
    }

}
