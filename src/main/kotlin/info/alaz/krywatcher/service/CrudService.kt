package info.alaz.krywatcher.service

import info.alaz.krywatcher.db.entity.Identifiable

interface CrudService<R : Identifiable<I>, I : java.io.Serializable> {

    fun upsert(newRecord: R): R {
        var oldRecord: R? = findById(newRecord.getIdentity())

        return when (oldRecord) {
            null -> insert(newRecord)
            else -> update(oldRecord, newRecord)
        }
    }

    fun findById(id: I): R?
    fun insert(record: R): R
    fun update(oldRecord: R, newRecord: R): R
    // TODO delete
}
