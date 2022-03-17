package info.alaz.krywatcher.db.entity

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Version
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.Table

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tb_http_link_status")
data class HttpLinkStatus(
    @Id @Column(name = "http_link_id", nullable = false) val httpLinkId: String,
    //@OneToOne @PrimaryKeyJoinColumn(name = "http_link_id", referencedColumnName = "id") val httpLink: HttpLink?,
    @Version @Column(name = "version", nullable = false) var version: Long,
    @Enumerated(EnumType.STRING) @Column(name = "last_status", nullable = false) var lastStatus: Status,
    @Column(name = "last_fail_date", nullable = true)
    var lastFailDate: ZonedDateTime = ZonedDateTime.now(),
    @Column(name = "last_ok_date", nullable = true)
    var lastOkDate: ZonedDateTime = ZonedDateTime.now()
) : Identifiable<String> {

    override fun getIdentity(): String {
        return httpLinkId
    }

}

enum class Status {
    OK, FAIL
}
