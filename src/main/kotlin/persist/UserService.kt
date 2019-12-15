package persist

import java.time.LocalDate

private const val SCHEMA_NAME = "interview"
private const val TABLE_REF = "interview.users"

private const val CREATE_SCHEMA_QUERY = "create schema if not exists $SCHEMA_NAME;"
private const val CREATE_TABLE_QUERY = """create table if not exists $TABLE_REF
                                            (
                                            	id serial not null
                                            		constraint users_pk
                                            			primary key,
                                            	name varchar not null,
                                            	birthdate date not null
                                            );"""
private const val USER_COUNT_QUERY = "select count(1) from $TABLE_REF;"
private const val FIRST_USER_QUERY = "insert into $TABLE_REF (name, birthdate) values ('Иванов Иван', '1990-01-01');"
private const val SECOND_USER_QUERY = "insert into $TABLE_REF (name, birthdate) values ('Олегов Олег', '1991-08-12');"
private const val THIRD_USER_QUERY = "insert into $TABLE_REF (name, birthdate) values ('Алекс Алексей', '2001-12-31');"


class UserService(private var jdbcConfiguration: JdbcConfiguration) {

    init {
        initDatabase()
        initData()
    }

    fun countUserByBirthdate(birthdate: LocalDate) = runCatching {
        val connection = jdbcConfiguration.getConnection()
        var count = 0
        connection.use {
            val preparedStatement = connection
                .prepareStatement("select count(1) from $TABLE_REF where extract(year from birthdate) = ?")
            preparedStatement.setInt(1, birthdate.year)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                count = resultSet.getInt(1)
            }
        }
        count
    }.map {
        it
    }.getOrElse { throw RuntimeException("Error while extracting data from database\n" + it.message) }

    private fun initDatabase() = runCatching {
        jdbcConfiguration.getConnection().use { connection ->
            connection.prepareStatement(CREATE_SCHEMA_QUERY).executeUpdate()
            connection.prepareStatement(CREATE_TABLE_QUERY.trimMargin()).executeUpdate()
        }

    }.getOrElse { throw RuntimeException("Smth wrong while creating database!\n" + it.message) }

    private fun initData() = runCatching {
        jdbcConfiguration.getConnection().use { connection ->
            var count = 0
            val result = connection.createStatement().executeQuery(USER_COUNT_QUERY)
            while (result.next()) {
                count = result.getInt(1)
            }
            if (count == 0) {
                for (i in 0..10) {
                    connection.createStatement().executeUpdate(FIRST_USER_QUERY)
                }
                for (i in 0..3) {
                    connection.createStatement().executeUpdate(SECOND_USER_QUERY)
                }
                for (i in 0..17) {
                    connection.createStatement().executeUpdate(THIRD_USER_QUERY)
                }
            }
        }

    }.getOrElse { throw RuntimeException("Smth wrong while data initialization!\n" + it.message) }
}