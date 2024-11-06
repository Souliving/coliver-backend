package coliver.services

import coliver.dao.users.UserDAO
import coliver.dto.CreateUserDto
import coliver.dto.FillUserDto
import coliver.dto.LkInfoDto
import coliver.model.User

class UserService(private val userDAO: UserDAO) {
    suspend fun getAll(): List<User> = userDAO.getAll()
    suspend fun getById(id: Long): User = userDAO.getUser(id)!!
    suspend fun fillUser(id: Long, dto: FillUserDto): Int = userDAO.fillUser(id, dto)
    suspend fun getLkById(id: Long): LkInfoDto = userDAO.getLkById(id)
    suspend fun getByEmail(email: String): User? = userDAO.getByEmail(email)
    suspend fun createUser(dto: CreateUserDto): Long? = userDAO.createUser(dto)
}
