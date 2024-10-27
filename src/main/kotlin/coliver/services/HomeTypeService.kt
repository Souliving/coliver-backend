package coliver.services

import coliver.dao.home_type.HomeTypeDao
import coliver.model.HomeType

class HomeTypeService(private val homeTypeDao: HomeTypeDao) {
    suspend fun getHomeTypes(): List<HomeType> = homeTypeDao.getAll()

    suspend fun getHomeTypeById(id: Long): HomeType = homeTypeDao.getById(id)
}
