package coliver.dao.home_type

import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.HomeTypeFormDto
import coliver.model.HomeType
import coliver.model.HomeTypeForms
import coliver.model.HomeTypes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class HomeTypeDAOImpl : HomeTypeDAO {
    private fun resultRowToHomeType(row: ResultRow) =
        HomeType(
            id = row[HomeTypes.id],
            name = row[HomeTypes.name],
        )

    override suspend fun getAll(): List<HomeType> =
        dbQuery {
            HomeTypes.selectAll().map(::resultRowToHomeType)
        }

    override suspend fun getById(id: Long): HomeType =
        dbQuery {
            HomeTypes
                .selectAll()
                .where(HomeTypes.id.eq(id))
                .map(::resultRowToHomeType)
                .first()
        }

    override suspend fun updateForForm(dto: HomeTypeFormDto) =
        transaction {
            dto.homeTypeIds.forEach { homeType ->
                HomeTypeForms.insert {
                    it[formId] = dto.formId
                    it[homeTypeId] = homeType
                }
            }
            return@transaction
        }

    override suspend fun deleteForForm(id: Long) =
        transaction {
            return@transaction HomeTypeForms.deleteWhere { HomeTypes.id eq id }
        }
}
