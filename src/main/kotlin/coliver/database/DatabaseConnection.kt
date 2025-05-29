package coliver.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

object DatabaseFactory {
    fun init(databaseParams: ConnectionParams) {
        initDB(databaseParams)
        transaction {
//            SchemaUtils.create(Images)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.Loom) { block() }

    private fun initDB(databaseParams: ConnectionParams) {
        val config =
            HikariConfig().apply {
                jdbcUrl = databaseParams.url
                username = databaseParams.user
                password = databaseParams.password
                maximumPoolSize = 32
            }
        val ds = HikariDataSource(config)
        Database.connect(ds)
    }
}

object LoomDispatcher : ExecutorCoroutineDispatcher() {
    override val executor: Executor =
        Executor { command ->
            Thread.startVirtualThread(command)
        }

    override fun dispatch(
        context: CoroutineContext,
        block: java.lang.Runnable
    ) {
        executor.execute(block)
    }

    override fun close() {
        error("Cannot be invoked on Dispatchers.LOOM")
    }
}

val Dispatchers.Loom: CoroutineDispatcher
    get() = LoomDispatcher
