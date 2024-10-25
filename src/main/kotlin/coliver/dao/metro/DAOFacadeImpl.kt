package coliver.dao.metro

import coliver.database.DatabaseFactory.dbQuery
import coliver.model.Metro
import coliver.model.Metros

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DAOFacadeImpl: DAOFacade {

    private fun resultRowToMetro(row: ResultRow) = Metro(
        id = row[Metros.id],
        name = row[Metros.name],
        cityId = row[Metros.cityId]
    )

    override suspend fun getAll(): List<Metro> = dbQuery {
        transaction {
            Metros.selectAll().map(::resultRowToMetro)
        }
    }
}


val metroDao = DAOFacadeImpl()
