package coliver.services

import coliver.dao.users.UserDAO
import coliver.model.User

class UserService(private val userDAO: UserDAO) {
    suspend fun getAll(): List<User> = userDAO.getAll()
    suspend fun getById(id: Long): User = userDAO.getUser(id)!!
}
