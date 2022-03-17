package info.alaz.krywatcher.validation

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class ValidationError(val fieldErrors: MutableList<FieldError> = mutableListOf()) {

    fun isValid(): Boolean {
        return fieldErrors.isEmpty()
    }

    fun addValidationError(fieldName: String, errorReason: String) {
        fieldErrors.add(FieldError(fieldName, errorReason))
    }

    fun getValidationErrors(): String {
        return fieldErrors.toString()
    }

    fun mergeValidationErrors(otherValidationError: ValidationError) {
        fieldErrors.addAll(otherValidationError.fieldErrors)
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FieldError(val fieldName: String, val errorReason: String)
