package coliver.dao.home_type

import coliver.database.DatabaseFactory.dbQuery
import coliver.model.HomeType
import coliver.model.HomeTypes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll

class HomeTypeDAOImpl : HomeTypeDAO {

    private fun resultRowToHomeType(row: ResultRow) = HomeType(
        id = row[HomeTypes.id],
        name = row[HomeTypes.name],
    )

    override suspend fun getAll(): List<HomeType> = dbQuery {
        HomeTypes.selectAll().map(::resultRowToHomeType)
    }

    override suspend fun getById(id: Long): HomeType = dbQuery {
        HomeTypes.selectAll()
            .where(HomeTypes.id.eq(id))
            .map(::resultRowToHomeType)
            .first()
    }
}
