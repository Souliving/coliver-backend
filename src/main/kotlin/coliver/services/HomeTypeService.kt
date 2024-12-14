package coliver.services

import coliver.dao.home_type.HomeTypeDAO
import coliver.dto.HomeTypeFormDto
import coliver.model.HomeType

class HomeTypeService(
    private val homeTypeDao: HomeTypeDAO
) {
    suspend fun getHomeTypes(): List<HomeType> = homeTypeDao.getAll()

    suspend fun getHomeTypeById(id: Long): HomeType = homeTypeDao.getById(id)

    suspend fun updateForForm(dto: HomeTypeFormDto) = homeTypeDao.updateForForm(dto)

    suspend fun deleteForForm(id: Long) = homeTypeDao.deleteForForm(id)
}
