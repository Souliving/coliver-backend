package coliver.dao.form.impl

import coliver.dao.form.ShortFormDAO
import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto
import coliver.model.*
import coliver.utils.buildFilterRequest
import coliver.utils.buildFilterRequestWithoutId
import coliver.utils.execAndMap
import coliver.utils.mapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet

class ShortFormDAOImpl : ShortFormDAO {

    private fun ResultSet.resultSetToShortForm(): ShortFormDto = ShortFormDto(
        id = this.getLong("id"),
        name = this.getString("name"),
        age = this.getInt("age"),
        city = City(
            this.getLong("cityId"),
            this.getString("cityName")
        ),
        metro = parseMetro(this.getString("metro")),
        budget = this.getLong("budget"),
        description = this.getString("description"),
        dateMove = this.getTimestamp("dateMove").toLocalDateTime().toKotlinLocalDateTime(),
        properties = Property(
            this.getLong("propertiesId"),
            this.getBoolean("smoking"),
            this.getBoolean("alcohol"),
            this.getBoolean("petFriendly"),
            this.getBoolean("isClean"),
            this.getLong("homeOwnerId")
        ),
        photoId = this.getLong("photoId"),
        onlineDateTime = this.getTimestamp("onlineDateTime").toLocalDateTime().toKotlinLocalDateTime()
    )

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

    override suspend fun getAll(): List<ShortFormDto> = transaction {
        val sql = "select * from get_short_forms()"
        getShortFormsWithSQL(sql)
    }

    override suspend fun getById(id: Long): ShortFormDto = transaction {
        val sql = "select * from get_short_forms_by_form_id($id)"
        getShortFormsWithSQL(sql).first()
    }

    override suspend fun getForUser(userId: Long): List<ShortFormDto> = transaction {
        val sql = "select * from get_short_forms_for_user_id($userId)"
        getShortFormsPersonalizedWithSQL(sql)
    }

    override suspend fun getWithFilter(userId: Long, filter: FilterDto): List<ShortFormDto> = transaction {
        val sql = buildFilterRequest(userId, filter)
        getShortFormsPersonalizedWithSQL(sql)
    }

    override suspend fun getWithFilterWithoutId(filter: FilterDto): List<ShortFormDto> = transaction {
        val sql = buildFilterRequestWithoutId(filter)
        getShortFormsPersonalizedWithSQL(sql)
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


    private fun getShortFormsWithSQL(sql: String): MutableList<ShortFormDto> {
        val result = mutableListOf<ShortFormDto>()
        sql.execAndMap { rs ->
            result.add(rs.resultSetToShortForm())
        }
        return result
    }

    private fun getShortFormsPersonalizedWithSQL(sql: String): MutableList<ShortFormDto> {
        val result = mutableListOf<ShortFormDto>()
        sql.execAndMap { rs ->
            result.add(rs.resultRowToShortFormPersonalized())
        }
        return result
    }

    private fun parseMetro(metroJson: String): List<Metro> {
        val model: List<Map<String, String>> = mapper.readValue(metroJson)
        val metroList = mutableListOf<Metro>()
        model.forEach { metro ->
            metroList.add(Metro(metro["id"]?.toLong(), metro["name"].toString(), metro["cityId"]?.toLong()))
        }
        return metroList
    }

}

fun ResultSet.resultRowToShortFormPersonalized(): ShortFormDto = ShortFormDto(
    id = this.getLong("id"),
    name = this.getString("name"),
    age = this.getInt("age"),
    city = City(
        this.getLong("cityId"),
        this.getString("cityName")
    ),
    metro = Json.decodeFromString(this.getString("metro")),
    budget = this.getLong("budget"),
    description = this.getString("description"),
    dateMove = this.getTimestamp("dateMove").toLocalDateTime().toKotlinLocalDateTime(),
    properties = Property(
        this.getLong("propertiesId"),
        this.getBoolean("smoking"),
        this.getBoolean("alcohol"),
        this.getBoolean("petFriendly"),
        this.getBoolean("isClean"),
        this.getLong("homeOwnerId")
    ),
    photoId = this.getLong("photoId"),
    onlineDateTime = this.getTimestamp("onlineDateTime").toLocalDateTime().toKotlinLocalDateTime(),
    isFavorite = this.getBoolean("isFavorite"),
)
