package coliver.dao.form.impl

import coliver.dao.form.FavFormDAO
import coliver.dto.form.ShortFormDto
import coliver.utils.execAndMap
import org.jetbrains.exposed.sql.transactions.transaction

class FavFormDAOImpl : FavFormDAO {
    override suspend fun getByUserId(userId: Long): List<ShortFormDto> = transaction {
        val sql = "select * from get_fav_forms_by_user_id($userId)"
        getShortFormsWithSQL(sql)
    }

    override suspend fun add(userId: Long, favFormId: Long) = transaction {
        val sql = "INSERT INTO favorite_forms(user_id, fav_form_id) VALUES ($userId, ${favFormId})"
        sql.execAndMap { }
        return@transaction
    }

    override suspend fun delete(userId: Long, favFormId: Long) = transaction {
        val sql = "DELETE FROM favorite_forms WHERE user_id = $userId and fav_form_id = $favFormId"
        sql.execAndMap { }
        return@transaction
    }

    private fun getShortFormsWithSQL(sql: String): MutableList<ShortFormDto> {
        val result = mutableListOf<ShortFormDto>()
        sql.execAndMap { rs ->
            result.add(rs.resultRowToShortFormPersonalized())
        }
        return result
    }
}
