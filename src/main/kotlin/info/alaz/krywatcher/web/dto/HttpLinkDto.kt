package info.alaz.krywatcher.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import info.alaz.krywatcher.db.entity.SupportedHttpMethod
import info.alaz.krywatcher.validation.ValidationError
import info.alaz.krywatcher.validation.Validator
import java.net.URL

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
                    URL(dataObject.url)
                } catch (e: Exception) {
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


