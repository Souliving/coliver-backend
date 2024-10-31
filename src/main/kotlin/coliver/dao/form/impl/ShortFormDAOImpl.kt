package coliver.dao.form.impl

import coliver.dao.form.ShortFormDAO
import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto
import coliver.model.City
import coliver.model.Property
import coliver.utils.buildFilterRequest
import coliver.utils.execAndMap
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet

class ShortFormDAOImpl : ShortFormDAO {

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
        getShortFormsWithSQL(sql)
    }

    override suspend fun getWithFilter(userId: Long, filter: FilterDto): List<ShortFormDto> = transaction {
        val sql = buildFilterRequest(userId, filter)
        getShortFormsWithSQL(sql)
    }

    private fun getShortFormsWithSQL(sql: String): MutableList<ShortFormDto> {
        val result = mutableListOf<ShortFormDto>()
        sql.execAndMap { rs ->
            result.add(rs.resultRowToShortForm())
        }
        return result
    }

}

fun ResultSet.resultRowToShortForm(): ShortFormDto = ShortFormDto(
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
