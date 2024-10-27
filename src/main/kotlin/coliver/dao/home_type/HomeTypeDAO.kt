package coliver.dao.home_type

import coliver.model.HomeType

interface HomeTypeDAO {
    suspend fun getAll(): List<HomeType>
    suspend fun getById(id: Long): HomeType
}
