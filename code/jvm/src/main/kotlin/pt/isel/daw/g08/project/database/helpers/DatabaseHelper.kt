package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import pt.isel.daw.g08.project.exceptions.NotFoundException
import java.lang.StringBuilder

private const val GET_PAGINATED_SUFFIX = "LIMIT :limit OFFSET :offset"

abstract class DatabaseHelper {

    @Autowired
    protected lateinit var jdbi: Jdbi

    protected fun <T> getList(query: String, mapTo: Class<T>, page: Int, perPage: Int, vararg bindPairs: Pair<String, Any>): List<T> =
        jdbi.withHandle<List<T>, Exception> {
            val handle = it.createQuery("$query $GET_PAGINATED_SUFFIX")
                .bind("limit", perPage)
                .bind("offset", page * perPage)
            bindPairs.forEach { pair -> handle.bind(pair.first, pair.second) }

            handle
                .mapTo(mapTo)
                .list()
        }

    protected fun <T> getOne(query: String, mapTo: Class<T>, vararg bindPairs: Pair<String, Any>): T =
        jdbi.withHandle<T, Exception> {
            val handle = it.createQuery(query)
            bindPairs.forEach { pair -> handle.bind(pair.first, pair.second) }

            val res = handle
                .mapTo(mapTo)
                .findOne()

            if (res.isEmpty) throw NotFoundException("Resource does not exist")
            res.get()
        }

    protected fun <T> insert(query: String, generatedIdType: Class<T>, vararg bindPairs: Pair<String, Any>): T =
        jdbi.withHandle<T, Exception> {
            val handle = it.createUpdate(query)
            bindPairs.forEach { pair -> handle.bind(pair.first, pair.second) }

            handle
                .executeAndReturnGeneratedKeys()
                .mapTo(generatedIdType)
                .one()
        }

    protected fun update(queryStart: String, updateFields: List<Pair<String, Any>>,
                         queryEnd: String, endBinds: List<Pair<String, Any>>) {
        // Build query string
        val stringBuilder = StringBuilder(queryStart)
        updateFields.forEach { stringBuilder.append(" ${it.first} = :${it.first},") }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        stringBuilder.append(' ')
        stringBuilder.append(queryEnd)

        jdbi.useHandle<Exception> {
            val handle = it.createUpdate(stringBuilder.toString())
            updateFields.forEach { pair -> handle.bind(pair.first, pair.second) }
            endBinds.forEach { pair -> handle.bind(pair.first, pair.second) }

            handle.execute()
        }
    }
}