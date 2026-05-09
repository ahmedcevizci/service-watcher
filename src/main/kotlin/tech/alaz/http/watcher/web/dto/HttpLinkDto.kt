package tech.alaz.http.watcher.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import tech.alaz.http.watcher.db.entity.SupportedHttpMethod
import tech.alaz.http.watcher.validation.ValidationError
import tech.alaz.http.watcher.validation.Validator
import java.net.URI
import java.net.URL

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HttpLinkRequestDto(
    var name: String,
    var url: String,
    var user: String,
    var method: SupportedHttpMethod = SupportedHttpMethod.GET,
    var successStatus: Int = 200,
)

object HttpLinkRequestValidator : Validator<HttpLinkRequestDto> {

    override fun validate(dataObject: HttpLinkRequestDto?): ValidationError {
        val validationError = ValidationError()
        if (dataObject == null) {
            validationError.addValidationError("newHttpLink", " is null.")
        } else {
            if (dataObject.name.isBlank()) {
                validationError.addValidationError("newHttpLink.name", " is blank.")
            }

            if (dataObject.url.isBlank()) {
                validationError.addValidationError("newHttpLink.url", " is blank.")
            } else {
                try {
                    URI.create(dataObject.url).toURL()
                } catch (_: Exception) {
                    validationError.addValidationError(
                        "newHttpLink.url",
                        " is not a valid HTTP URL."
                    )
                }
            }

//            if (dataObject.method.isBlank() ) {
//                validationError.addValidationError("newHttpLink.method", " is blank.")
//            }

            if (dataObject.successStatus < 100 || dataObject.successStatus >= 600) {
                validationError.addValidationError("newHttpLink.successStatus", " should be between 100 and 599.")
            }
        }

        return validationError;
    }

}

data class HttpLinkResponseDto(val url: URL) {
}

data class HttpLinkStatusResponseDto(val url: URL) {

}


