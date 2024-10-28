package coliver.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseFactory {
    fun init(databaseParams: ConnectionParams) {
        initDB(databaseParams)
        transaction {
//            SchemaUtils.create(Images)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun initDB(databaseParams: ConnectionParams) {

        val config = HikariConfig().apply {
            jdbcUrl = databaseParams.url
            username = databaseParams.user
            password = databaseParams.password
            maximumPoolSize = 3
        }
        val ds = HikariDataSource(config)
        Database.connect(ds)
    }
}
