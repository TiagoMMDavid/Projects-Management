package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired

private const val GET_PAGINATED_SUFFIX = "LIMIT :limit OFFSET :offset"

abstract class DatabaseHelper {
    @Autowired
    protected lateinit var jdbi: Jdbi

    protected fun <T> getList(page: Int, perPage: Int, query: String, mapTo: Class<T>): List<T> =
        jdbi.withHandle<List<T>, Exception> {
            it.createQuery("$query $GET_PAGINATED_SUFFIX")
                .bind("limit", perPage)
                .bind("offset", page * perPage)
                .mapTo(mapTo)
                .list()
        }

    protected fun <T, V> boundedGetList(page: Int, perPage: Int, query: String, bindName: String, bindValue: V, mapTo: Class<T>): List<T> =
        jdbi.withHandle<List<T>, Exception> {
            it.createQuery("$query $GET_PAGINATED_SUFFIX")
                .bind("limit", perPage)
                .bind("offset", page * perPage)
                .bind(bindName, bindValue)
                .mapTo(mapTo)
                .list()
        }

    protected fun <T> getOne(query: String, mapTo: Class<T>): T =
        jdbi.withHandle<T, Exception> {
            it.createQuery(query)
                .mapTo(mapTo)
                .one()
        }

    protected fun <T, V> boundedGetOne(bindName: String, bindValue: V, query: String, mapTo: Class<T>): T =
        jdbi.withHandle<T, Exception> {
            it.createQuery(query)
                .bind(bindName, bindValue)
                .mapTo(mapTo)
                .one()
        }
}