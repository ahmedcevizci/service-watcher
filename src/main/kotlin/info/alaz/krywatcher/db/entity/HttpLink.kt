package info.alaz.krywatcher.db.entity

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(
    name = "tb_http_link",
    uniqueConstraints = [UniqueConstraint(name = "unique_url", columnNames = ["url"])]
)
data class HttpLink(
    @Id @Column(name = "id", nullable = false) val id: String,
    @Version @Column(name = "version", nullable = false) var version: Long,
    @Column(name = "name", nullable = false) var name: String,
    @Column(name = "url", nullable = false) var url: String, // TODO java.net.URL
    //@Column(name = "url_parameters", nullable = true) var urlParameters: String, TODO coma separated key-value pairs for POST,PUT,PATCH
    @ManyToOne @JoinColumn(name = "user", referencedColumnName = "name", nullable = false) val user: KryUser,
    @Enumerated(EnumType.STRING) @Column(
        name = "method",
        nullable = false
    ) var method: SupportedHttpMethod = SupportedHttpMethod.GET,
    @Column(name = "success_status", nullable = false) var successStatus: Int = 200,
    //val range: Boolean = false, // e.g. 2XX, 3XX etc.
    //@OneToOne val httpLinkStatus: HttpLinkStatus,
    @CreatedDate @Column(name = "created_date", nullable = false)
    var createdDate: ZonedDateTime = ZonedDateTime.now(),
    @LastModifiedDate @Column(name = "last_modified_date", nullable = false)
    var lastModifiedDate: ZonedDateTime = ZonedDateTime.now()
) : Identifiable<String> {

    override fun getIdentity(): String {
        return id
    }

}

enum class SupportedHttpMethod {
    GET, HEAD //, DELETE, POST, PUT, PATCH, OPTIONS, TRACE
}

