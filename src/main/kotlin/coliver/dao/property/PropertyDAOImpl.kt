package coliver.dao.property

import coliver.database.DatabaseFactory.dbQuery
import coliver.model.Properties
import coliver.model.Property
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

    override suspend fun getById(id: Long): Property = dbQuery {
        transaction {
            Properties.selectAll()
                .where(Properties.id.eq(id))
                .map(::resultRowToProperty)
                .single()
        }
    }
}
