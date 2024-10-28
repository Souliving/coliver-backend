package coliver.dao.metro

import coliver.database.DatabaseFactory.dbQuery
import coliver.model.Metro
import coliver.model.Metros
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll

class MetroDAOImpl : MetroDAO {

    private fun resultRowToMetro(row: ResultRow) = Metro(
        id = row[Metros.id],
        name = row[Metros.name],
        cityId = row[Metros.cityId]
    )

    override suspend fun getAll(): List<Metro> = dbQuery {
        Metros.selectAll().map(::resultRowToMetro)
    }

    override suspend fun getMetro(metroId: Long): Metro = dbQuery {
        Metros.selectAll().where(Metros.id.eq(metroId)).map(::resultRowToMetro).single()
    }

    override suspend fun getMetroByCityId(cityId: Long): List<Metro> = dbQuery {
        Metros.selectAll().where(Metros.cityId.eq(cityId)).map(::resultRowToMetro)
    }

    override suspend fun getMetroByName(metroName: String): Metro = dbQuery {
        Metros.selectAll().where(Metros.name.eq(metroName)).map(::resultRowToMetro).single()
    }
}
