package coliver.dao.form.impl


import coliver.dao.form.ShortFormDAO
import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto
import coliver.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ShortFormDAOImpl : ShortFormDAO {
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

    private fun resultRowToShortForm(row: ResultRow): ShortFormDto {
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
            isFavorite = false
        )
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
            isFavorite = row[FavoriteForms.favFormId] != null
        )
    }

    private fun resultRowToFormDto(row: ResultRow): Form = Form(
        id = row[Forms.id],
        userId = row[Forms.userId],
        description = row[Forms.description],
        reviews = listOf(row[Forms.reviews]),
        photoId = row[Forms.photoId],
        propertiesId = row[Forms.propertiesId],
        cityId = row[Forms.cityId],
        budget = row[Forms.budget],
        dateMove = row[Forms.dateMove],
        onlineDateTime = row[Forms.onlineDateTime],
        rating = row[Forms.rating]
    )

    private fun getShortForms() = Forms.join(
        Users, joinType = JoinType.INNER, onColumn =
        Forms.userId, otherColumn = Users.id
    )
        .join(Properties, JoinType.INNER, onColumn = Forms.propertiesId, otherColumn = Properties.id)
        .join(Cities, JoinType.INNER, onColumn = Forms.cityId, otherColumn = Cities.id)
        .join(FormMetroIds, JoinType.LEFT, onColumn = Forms.id, otherColumn = FormMetroIds.formId)
        .join(Metros, JoinType.LEFT, onColumn = Metros.id, otherColumn = FormMetroIds.metroId)
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
            metroCityIdsAgg
        )
        .groupBy(
            Forms.id, Users.id, Users.name, Users.age, Cities.id, Cities.name, Forms.budget,
            Forms.description, Forms.dateMove, Properties.id, Properties.smoking, Properties.alcohol,
            Properties.petFriendly, Properties.isClean, Properties.homeOwnerId, Forms.photoId,
            Forms.onlineDateTime
        )

    private fun getShortFormsWithFav(userId: Long) = Forms.join(
        Users, joinType = JoinType.INNER, onColumn =
        Forms.userId, otherColumn = Users.id
    )
        .join(Properties, JoinType.INNER, onColumn = Forms.propertiesId, otherColumn = Properties.id)
        .join(Cities, JoinType.INNER, onColumn = Forms.cityId, otherColumn = Cities.id)
        .join(FormMetroIds, JoinType.LEFT, onColumn = Forms.id, otherColumn = FormMetroIds.formId)
        .join(Metros, JoinType.LEFT, onColumn = Metros.id, otherColumn = FormMetroIds.metroId)
        .join(
            FavoriteForms,
            JoinType.LEFT,
            onColumn = FavoriteForms.favFormId,
            otherColumn = Forms.id,
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
        .andWhere { Users.id neq userId }

    private fun filterSQL() = Forms.join(
        Users, joinType = JoinType.INNER, onColumn =
        Forms.userId, otherColumn = Users.id
    )
        .join(Properties, JoinType.INNER, onColumn = Forms.propertiesId, otherColumn = Properties.id)
        .join(Cities, JoinType.INNER, onColumn = Forms.cityId, otherColumn = Cities.id)
        .join(FormMetroIds, JoinType.LEFT, onColumn = Forms.id, otherColumn = FormMetroIds.formId)
        .join(Metros, JoinType.LEFT, onColumn = Metros.id, otherColumn = FormMetroIds.metroId)
        .select(
            Forms.id,
        )
        .groupBy(
            Forms.id, Users.id, Users.name, Users.age, Cities.id, Cities.name, Forms.budget,
            Forms.description, Forms.dateMove, Properties.id, Properties.smoking, Properties.alcohol,
            Properties.petFriendly, Properties.isClean, Properties.homeOwnerId, Forms.photoId,
            Forms.onlineDateTime
        )


    override suspend fun getAll(): List<ShortFormDto> = dbQuery {
        getShortForms().map(::resultRowToShortForm)
    }

    override suspend fun getById(id: Long): ShortFormDto? = dbQuery {
        getShortForms().where(Forms.id eq id).map(::resultRowToShortForm).firstOrNull()
    }

    override suspend fun getForUser(userId: Long): List<ShortFormDto> = dbQuery {
        getShortFormsWithFav(userId).map(::resultRowToShortFormWithFavs)
    }

    override suspend fun getWithFilter(userId: Long, filterDto: FilterDto): List<ShortFormDto> = dbQuery {
        val filteringSQl = filterSQL()

        filteringSQl.andWhere { Forms.userId neq userId }

        val ids = findForms(filterDto, filteringSQl)

        val shortForms = getShortFormsWithFav(userId)

        shortForms.andWhere { Forms.id eq anyFrom(ids) }.map(::resultRowToShortFormWithFavs)
    }

    override suspend fun getWithFilterWithoutId(filterDto: FilterDto): List<ShortFormDto> = dbQuery {
        val filteringSQl = filterSQL()

        val ids = findForms(filterDto, filteringSQl)

        val shortForms = getShortForms()

        shortForms.where { Forms.id eq anyFrom(ids) }.map(::resultRowToShortForm)
    }

    override suspend fun save(newForm: Form): Form = transaction {
        val formIns = Forms.insert {
            it[userId] = newForm.userId!!
            it[description] = newForm.description
            it[rating] = newForm.rating
            it[dateMove] = newForm.dateMove
            it[reviews] = newForm.reviews.toString()
            it[photoId] = newForm.photoId!!
            it[propertiesId] = newForm.propertiesId!!
            it[cityId] = newForm.cityId!!
            it[budget] = newForm.budget
            it[dateMove] = newForm.dateMove
            it[onlineDateTime] = newForm.onlineDateTime
        }
        formIns.resultedValues?.singleOrNull()?.let { resultRowToFormDto(it) }!!
    }

    override suspend fun delete(id: Long) = dbQuery {
        transaction {
            Forms.deleteWhere { Forms.id eq id }
        }
    }

    private fun findForms(filterDto: FilterDto, filteringSQl: Query): List<Long> {
        val fullFilterQuery = buildFilter(filterDto, filteringSQl)

        val ids = mutableListOf<Long>()
        fullFilterQuery.map { ids.add(it[Forms.id]) }
        return ids
    }

    private fun buildFilter(filterDto: FilterDto, filteringSQL: Query): Query {
        if (filterDto.cityId.isNotEmpty()) {
            filteringSQL.andWhere { Forms.cityId eq anyFrom(filterDto.cityId) }
        }
        if (filterDto.metroIds.isNotEmpty()) {
            filteringSQL.andWhere { FormMetroIds.metroId eq anyFrom(filterDto.metroIds) }
        }
        if (filterDto.price.startPrice != null) {
            filteringSQL.andWhere { Forms.budget greaterEq filterDto.price.startPrice }
        }
        if (filterDto.price.endPrice != null) {
            filteringSQL.andWhere { Forms.budget lessEq filterDto.price.endPrice }
        }
        if (filterDto.age.startAge != null) {
            filteringSQL.andWhere { Users.age greaterEq filterDto.age.startAge }
        }
        if (filterDto.age.endAge != null) {
            filteringSQL.andWhere { Users.age lessEq filterDto.age.endAge }
        }
        if (filterDto.alcohol != null) {
            filteringSQL.andWhere { Properties.alcohol eq filterDto.alcohol }
        }
        if (filterDto.smoking != null) {
            filteringSQL.andWhere { Properties.smoking eq filterDto.smoking }
        }
        if (filterDto.petFriendly != null) {
            filteringSQL.andWhere { Properties.petFriendly eq filterDto.petFriendly }
        }
        if (filterDto.isClean != null) {
            filteringSQL.andWhere { Properties.isClean eq filterDto.isClean }
        }
        return filteringSQL
    }
}


