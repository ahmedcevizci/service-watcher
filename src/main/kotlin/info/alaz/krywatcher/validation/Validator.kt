package info.alaz.krywatcher.validation

interface Validator <T> {

    fun validate(dataObject: T?): ValidationError

}
