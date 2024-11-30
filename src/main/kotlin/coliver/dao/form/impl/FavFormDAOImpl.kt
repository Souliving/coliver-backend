package coliver.dao.form.impl

import coliver.dao.form.FavFormDAO
import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.form.ShortFormDto
import coliver.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class FavFormDAOImpl : FavFormDAO {

    private val metroIds = Metros.id.castTo(VarCharColumnType())
    private val metroCityIds = Metros.cityId.castTo(VarCharColumnType())

    private val metroIdsAgg = metroIds.groupConcat(",", distinct = true)
    private val metroNamesAgg = Metros.name.groupConcat(",", distinct = true)
    private val metroCityIdsAgg = metroCityIds.groupConcat(",", distinct = true)

    private fun aggregateMetros(ids: String, names: String, cityId: String): List<Metro> {
        val listOfMetros = mutableListOf<Metro>()

        val metroIds = ids.split(',')
        val metroNames = names.split(',')
        val cityIds = cityId.split(',')

        metroIds.forEachIndexed { index, s ->
            listOfMetros.add(Metro(s.toLong(), metroNames[index], cityIds.first().toLong()))
        }

        return listOfMetros
    }

    private fun resultRowToShortFormWithFavs(row: ResultRow): ShortFormDto {
        return ShortFormDto(
            id = row[Forms.id],
            name = row[Users.name],
            age = row[Users.age],
            city = City(row[Cities.id], row[Cities.name]),
            metro = aggregateMetros(row[metroIdsAgg], row[metroNamesAgg], row[metroCityIdsAgg]),
            budget = row[Forms.budget],
            description = row[Forms.description],
            properties = Property(
                id = row[Properties.id],
                smoking = row[Properties.smoking],
                alcohol = row[Properties.alcohol],
                petFriendly = row[Properties.petFriendly],
                isClean = row[Properties.isClean],
                homeOwnerId = row[Properties.homeOwnerId],
            ),
            photoId = row[Forms.photoId],
            dateMove = row[Forms.dateMove],
            onlineDateTime = row[Forms.onlineDateTime],
            isFavorite = true
        )
    }

    private fun getFavShortForms(userId: Long) = Forms.join(
        Users, joinType = JoinType.INNER, onColumn =
        Forms.userId, otherColumn = Users.id
    )
        .join(Properties, JoinType.INNER, onColumn = Forms.propertiesId, otherColumn = Properties.id)
        .join(Cities, JoinType.INNER, onColumn = Forms.cityId, otherColumn = Cities.id)
        .join(FormMetroIds, JoinType.LEFT, onColumn = Forms.id, otherColumn = FormMetroIds.formId)
        .join(Metros, JoinType.LEFT, onColumn = Metros.id, otherColumn = FormMetroIds.metroId)
        .join(FavoriteForms, JoinType.INNER, onColumn = FavoriteForms.favFormId, otherColumn = Forms.id,
            additionalConstraint = { FavoriteForms.userId eq userId })
        .select(
            Forms.id,
            Users.name,
            Users.age,
            Cities.id,
            Cities.name,
            Forms.budget,
            Forms.description,
            Properties.id,
            Properties.alcohol,
            Properties.petFriendly,
            Properties.smoking,
            Properties.isClean,
            Properties.homeOwnerId,
            Forms.photoId,
            Forms.dateMove,
            Forms.onlineDateTime,
            metroIdsAgg,
            metroNamesAgg,
            metroCityIdsAgg,
            FavoriteForms.favFormId
        )
        .groupBy(
            Forms.id, Users.id, Users.name, Users.age, Cities.id, Cities.name, Forms.budget,
            Forms.description, Forms.dateMove, Properties.id, Properties.smoking, Properties.alcohol,
            Properties.petFriendly, Properties.isClean, Properties.homeOwnerId, Forms.photoId,
            Forms.onlineDateTime, FavoriteForms.favFormId
        )

    override suspend fun getByUserId(userId: Long): List<ShortFormDto> = dbQuery {
        getFavShortForms(userId).map(::resultRowToShortFormWithFavs)
    }

    override suspend fun add(userId: Long, favFormId: Long) = transaction {
        FavoriteForms.insert {
            it[FavoriteForms.userId] = userId
            it[FavoriteForms.favFormId] = favFormId
        }
        return@transaction
    }

    override suspend fun delete(userId: Long, favFormId: Long) = transaction {
        FavoriteForms.deleteWhere { FavoriteForms.userId eq userId and (FavoriteForms.favFormId eq favFormId) }
        return@transaction
    }

    override suspend fun deleteForForm(formId: Long) = transaction {
        FavoriteForms.deleteWhere { FavoriteForms.favFormId eq formId }
        return@transaction
    }
}

