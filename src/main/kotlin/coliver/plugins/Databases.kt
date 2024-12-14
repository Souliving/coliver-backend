package coliver.plugins

import coliver.database.ConnectionParams
import coliver.database.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.config.yaml.*

fun Application.configureDatabases() {
    val config = YamlConfig("application.yaml")
    val url = config!!.propertyOrNull("storage.jdbcURL")?.getString()!! // "jdbc:postgresql://localhost:5431/metros"//
    val user = config.propertyOrNull("storage.user")?.getString()!! // "george"//
    val password = config.propertyOrNull("storage.password")?.getString()!! // "40ehodas"//

    DatabaseFactory.init(
        ConnectionParams(
            url,
            user,
            password,
        ),
    )
}
