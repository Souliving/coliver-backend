package coliver.dao.users

import coliver.dto.CreateUserDto
import coliver.dto.FillUserDto
import coliver.dto.LkInfoDto
import coliver.model.User

interface UserDAO {
    suspend fun getAll(): List<User>
    suspend fun getUser(id: Long): User?
    suspend fun fillUser(id: Long, user: FillUserDto): Int
    suspend fun getLkById(id: Long): LkInfoDto
    suspend fun getByEmail(email: String): User?
    suspend fun createUser(dto: CreateUserDto): Long?
}
