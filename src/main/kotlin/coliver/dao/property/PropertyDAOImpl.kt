package coliver.dao.property

import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.CreatePropertiesDto
import coliver.model.Properties
import coliver.model.Property
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PropertyDAOImpl : PropertyDAO {

    private fun resultRowToProperty(row: ResultRow) = Property(
        id = row[Properties.id],
        smoking = row[Properties.smoking],
        alcohol = row[Properties.alcohol],
        petFriendly = row[Properties.petFriendly],
        isClean = row[Properties.isClean],
        homeOwnerId = row[Properties.homeOwnerId],
    )

    override suspend fun delete(id: Long): Int = dbQuery {
        transaction {
            Properties.deleteWhere { Properties.id eq id }
        }
    }

    override suspend fun getById(id: Long): Property = dbQuery {
        Properties.selectAll()
            .where(Properties.id.eq(id))
            .map(::resultRowToProperty)
            .single()
    }

    override suspend fun create(dto: CreatePropertiesDto): Long = transaction {
        val propIns = Properties.insert {
            it[smoking] = dto.smoking
            it[alcohol] = dto.alcohol
            it[petFriendly] = dto.petFriendly
            it[isClean] = dto.isClean
            it[homeOwnerId] = dto.homeOwnerId ?: 0
        }
        propIns.resultedValues?.singleOrNull()?.let { resultRowToProperty(it).id } ?: 0
    }
}
