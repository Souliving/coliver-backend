package coliver.dao.form.impl

import coliver.dao.form.FavFormDAO
import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.form.ShortFormDto
import coliver.model.*
import coliver.utils.getFavShortForms
import coliver.utils.resultRowToShortFormWithFavs
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class FavFormDAOImpl : FavFormDAO {

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

