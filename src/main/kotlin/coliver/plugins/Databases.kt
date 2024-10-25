package coliver.plugins

import coliver.database.ConnectionParams
import coliver.database.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.config.yaml.*

fun Application.configureDatabases() {
    val config = YamlConfig("application.yaml")
    val url = "jdbc:postgresql://94.103.89.23:5432/coliver"//config!!.propertyOrNull("storage.jdbcURL")?.getString()!!
    val user = "postgres"//config.propertyOrNull("storage.user")?.getString()!!
    val password = "B7Q4zAjtiadRyT"//config.propertyOrNull("storage.password")?.getString()!!

    DatabaseFactory.init(
        ConnectionParams(
            url, user, password
        )
    )
}
