package tech.alaz.http.watcher.validation

interface Validator <T> {

    fun validate(dataObject: T?): ValidationError

}
