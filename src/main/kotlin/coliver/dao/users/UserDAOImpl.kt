package coliver.dao.users

import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.CreateUserDto
import coliver.dto.FillUserDto
import coliver.dto.LkInfoDto
import coliver.model.Gender
import coliver.model.User
import coliver.model.UserRole
import coliver.model.Users
import coliver.utils.execAndMap
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.sql.Array
import java.time.LocalDateTime

class UserDAOImpl : UserDAO {
    private fun resultRowToUser(row: ResultRow) =
        User(
            id = row[Users.id],
            email = row[Users.email],
            password = row[Users.password],
            role = UserRole.valueOf(row[Users.role]),
            name = row[Users.name],
            age = row[Users.age],
            gender = Gender.valueOf(row[Users.gender]),
            enabled = row[Users.enabled],
            createDate = row[Users.createDate],
            modifyDate = row[Users.modifyDate],
        )

    override suspend fun getAll(): List<User> =
        dbQuery {
            Users.selectAll().map(::resultRowToUser)
        }

    override suspend fun getUser(id: Long): User? =
        dbQuery {
            Users
                .selectAll()
                .where { Users.id eq id }
                .map(::resultRowToUser)
                .singleOrNull()
        }

    override suspend fun fillUser(
        id: Long,
        user: FillUserDto
    ): Int =
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[email] = user.email
                it[age] = user.age
                it[gender] = user.gender.toString()
            }
        }

    override suspend fun getLkById(id: Long): LkInfoDto =
        dbQuery {
            val procedure = "select * from get_lk_info_for_user_id($id)"
            val lkInfoDto = LkInfoDto(0, "", listOf())
            procedure.execAndMap { rs ->
                lkInfoDto.formId = rs.getLong("formId")
                lkInfoDto.cityName = rs.getString("cityName")
                lkInfoDto.metroNames = rs.getArray("metroNames").toList()
            }
            lkInfoDto
        }

    override suspend fun getByEmail(email: String): User? =
        dbQuery {
            Users
                .selectAll()
                .where(Users.email.eq(email))
                .map(::resultRowToUser)
                .singleOrNull()
        }

    override suspend fun createUser(dto: CreateUserDto): Long? =
        dbQuery {
            val userInsert =
                Users.insert {
                    it[email] = dto.email
                    it[password] = dto.password
                    it[name] = dto.name
                    it[age] = dto.age
                    it[gender] = dto.gender.toString()
                    it[role] = UserRole.USER.toString()
                    it[createDate] = LocalDateTime.now().toKotlinLocalDateTime()
                    it[modifyDate] = LocalDateTime.now().toKotlinLocalDateTime()
                }
            userInsert.resultedValues?.singleOrNull()?.let { resultRowToUser(it).id }
        }
}

private fun Array.toList(): List<String> {
    var list = listOf<String>()
    listOf(this).forEach { arrayElement ->
        val tmp =
            arrayElement
                .toString()
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
        list = tmp.split(", ")
    }
    return list
}
