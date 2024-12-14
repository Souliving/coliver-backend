package coliver.dao.metro

import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.MetroFormDto
import coliver.model.FormMetroIds
import coliver.model.Metro
import coliver.model.Metros
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MetroDAOImpl : MetroDAO {
    private fun resultRowToMetro(row: ResultRow) =
        Metro(
            id = row[Metros.id],
            name = row[Metros.name],
            cityId = row[Metros.cityId],
        )

    override suspend fun getAll(): List<Metro> =
        dbQuery {
            Metros.selectAll().map(::resultRowToMetro)
        }

    override suspend fun getMetro(metroId: Long): Metro =
        dbQuery {
            Metros
                .selectAll()
                .where(Metros.id.eq(metroId))
                .map(::resultRowToMetro)
                .single()
        }

    override suspend fun getMetroByCityId(cityId: Long): List<Metro> =
        dbQuery {
            Metros.selectAll().where(Metros.cityId.eq(cityId)).map(::resultRowToMetro)
        }

    override suspend fun getMetroByName(metroName: String): Metro =
        dbQuery {
            Metros
                .selectAll()
                .where(Metros.name.eq(metroName))
                .map(::resultRowToMetro)
                .single()
        }

    override suspend fun updateMetrosInForm(dto: MetroFormDto) =
        transaction {
            FormMetroIds.deleteWhere {
                formId eq dto.formId
            }

            dto.metroIds.forEach { metro ->
                FormMetroIds.insert {
                    it[formId] = dto.formId
                    it[metroId] = metro
                }
            }

            return@transaction
        }

    override suspend fun deleteMetroForForm(id: Long) =
        transaction {
            return@transaction FormMetroIds.deleteWhere { formId eq id }
        }
}
