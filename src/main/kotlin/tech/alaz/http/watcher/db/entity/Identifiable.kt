package tech.alaz.http.watcher.db.entity

interface Identifiable<I: java.io.Serializable>{

    fun getIdentity(): I
}
