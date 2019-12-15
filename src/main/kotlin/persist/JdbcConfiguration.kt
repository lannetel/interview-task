package persist

import java.sql.Connection
import java.sql.DriverManager

private const val DRIVER = "org.postgresql.Driver"
private const val DB_URL = "jdbc:postgresql://localhost:5432/postgres"
private const val USERNAME = "postgres"
private const val PASSWORD = "mysecretpassword"

class JdbcConfiguration {
    private var connection: Connection? = null

    fun getConnection() = runCatching {
        if (connection == null || connection!!.isClosed) {
            Class.forName(DRIVER)
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)
        }
        connection!!
    }.map {
        it
    }.getOrElse { throw RuntimeException("Smth wrong while creating connection!\n" + it.message) }

}