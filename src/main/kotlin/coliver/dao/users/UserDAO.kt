package coliver.dao.users

import coliver.dto.FillUserDto
import coliver.model.User

interface UserDAO {
    suspend fun getAll(): List<User>
    suspend fun getUser(id: Long): User?
    suspend fun fillUser(id: Long, user: FillUserDto): Int
}
