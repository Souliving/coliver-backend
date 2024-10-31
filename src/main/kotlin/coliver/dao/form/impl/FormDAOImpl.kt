package coliver.dao.form.impl

import coliver.dao.form.FormDAO
import coliver.dto.form.FormDto
import coliver.dto.form.FullFormDto
import coliver.dto.form.ShortFormDto
import coliver.model.City
import coliver.model.HomeType
import coliver.model.Property
import coliver.utils.execAndMap
import coliver.utils.mapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet

class FormDAOImpl : FormDAO {
    private fun ResultSet.resultRowToForm(): FormDto = FormDto(
        id = this.getLong("id"),
        homeType = this.getString("homeType").parseHomeTypes(),
        rating = this.getDouble("rating"),
        reviews = listOf(this.getString("reviews")),
    )

    private fun ResultSet.resultRowToFullForm(): FullFormDto = FullFormDto(
        id = this.getLong("id"),
        userId = this.getLong("userId"),
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
        homeType = this.getString("homeType").parseHomeTypes(),
        socialMediaIds = null,
        rating = this.getDouble("rating"),
        reviews = listOf(this.getString("reviews")),
    )

    override suspend fun getAll(): List<FormDto> = transaction {
        val sql = "select * from get_forms()"
        getFormsWithSQL(sql)
    }


    override suspend fun getById(id: Long): List<FormDto> = transaction {
        val sql = "select * from get_forms_by_form_id($id)"
        getFormsWithSQL(sql)
    }

    override suspend fun getByUserId(userId: Long): List<FormDto> = transaction {
        val sql = "select * from get_forms_by_user_id($userId)"
        getFormsWithSQL(sql)
    }

    override suspend fun getFullFormById(id: Long): List<FullFormDto> = transaction {
        val sql = "select * from get_full_forms_by_form_id($id)"
        val result = mutableListOf<FullFormDto>()
        sql.execAndMap { rs ->
            result.add(rs.resultRowToFullForm())
        }
        result
    }

    override suspend fun create(userId: Long, form: FormDto): ShortFormDto {
        TODO("Not yet implemented")
    }

    private fun getFormsWithSQL(sql: String): MutableList<FormDto> {
        val result = mutableListOf<FormDto>()
        sql.execAndMap { rs ->
            result.add(rs.resultRowToForm())
        }
        return result
    }
}

private fun String.parseHomeTypes(): List<HomeType> {
    val model: List<Map<String, String>> = mapper.readValue(this)
    val homeTypes = mutableListOf<HomeType>()
    model.forEach { homeType ->
        homeTypes.add(HomeType(homeType["id"]?.toLong(), homeType["name"]!!))
    }
    return homeTypes
}


