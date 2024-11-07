package coliver.dao.home_type

import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.HomeTypeFormDto
import coliver.model.HomeType
import coliver.model.HomeTypes
import coliver.utils.exec
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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

    override suspend fun updateForForm(dto: HomeTypeFormDto) = transaction {
        dto.homeTypeIds.forEach { homeTypeId ->
            val sql = "INSERT INTO home_type_form(form_id, home_type_id) VALUES (${dto.formId}, $homeTypeId)"
            sql.exec()
        }
        return@transaction
    }

    override suspend fun deleteForForm(id: Long) = transaction {
        val deleteSQL = "DELETE FROM home_type_form WHERE form_id = $id"
        deleteSQL.exec()
        return@transaction
    }

}
