package info.alaz.krywatcher.db.entity

interface Identifiable<I: java.io.Serializable>{

    fun getIdentity(): I
}
