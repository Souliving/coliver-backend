package coliver.dao.users

import coliver.model.User

interface UserDAO {
    suspend fun getAll(): List<User>
    suspend fun getUser(id: Long): User?
}
