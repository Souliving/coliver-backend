package coliver.dao.users

import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.FillUserDto
import coliver.dto.LkInfoDto
import coliver.model.Gender
import coliver.model.User
import coliver.model.UserRole
import coliver.model.Users
import coliver.utils.execAndMap
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.sql.Array


class UserDAOImpl : UserDAO {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        email = row[Users.email],
        password = row[Users.password],
        role = UserRole.valueOf(row[Users.role]),
        name = row[Users.name],
        age = row[Users.age],
        gender = Gender.valueOf(row[Users.gender]),
        enabled = row[Users.enabled],
        createDate = row[Users.createDate],
        modifyDate = row[Users.modifyDate]
    )

    override suspend fun getAll(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun getUser(id: Long): User? = dbQuery {
        Users.selectAll().where { Users.id eq id }.map(::resultRowToUser).singleOrNull()
    }

    override suspend fun fillUser(id: Long, user: FillUserDto): Int = dbQuery {
        transaction {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[email] = user.email
                it[age] = user.age
                it[gender] = user.gender.toString()
            }
        }
    }

    override suspend fun getLkById(id: Long): LkInfoDto = transaction {
        val procedure = "select * from get_lk_info_for_user_id($id)"
        val lkInfoDto = LkInfoDto(0, "", listOf())
        procedure.execAndMap { rs ->
            lkInfoDto.formId = rs.getLong("formId")
            lkInfoDto.cityName = rs.getString("cityName")
            lkInfoDto.metroNames = rs.getArray("metroNames").toList()
        }
        lkInfoDto
    }
}

private fun Array.toList(): List<String> {
    var list = listOf<String>()
    listOf(this).forEach { arrayElement ->
        val tmp = arrayElement.toString().replace("[", "")
            .replace("]", "")
            .replace("\"", "")
        list = tmp.split(", ")
    }
    return list
}
