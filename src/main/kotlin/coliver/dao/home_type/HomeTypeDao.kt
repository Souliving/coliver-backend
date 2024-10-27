package coliver.dao.home_type

import coliver.model.HomeType

interface HomeTypeDao {
    suspend fun getAll(): List<HomeType>
    suspend fun getById(id: Long): HomeType
}
