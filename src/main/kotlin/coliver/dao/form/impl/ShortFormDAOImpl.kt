package coliver.dao.form.impl


import coliver.dao.form.ShortFormDAO
import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto
import coliver.model.*
import coliver.utils.filterSQL
import coliver.utils.getShortForms
import coliver.utils.getShortFormsWithFav
import coliver.utils.resultRowToFormDto
import coliver.utils.resultRowToShortForm
import coliver.utils.resultRowToShortFormWithFavs
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ShortFormDAOImpl : ShortFormDAO {

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


