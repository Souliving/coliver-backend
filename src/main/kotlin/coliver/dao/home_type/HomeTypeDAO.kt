package coliver.dao.home_type

import coliver.dto.HomeTypeFormDto
import coliver.model.HomeType

interface HomeTypeDAO {
    suspend fun getAll(): List<HomeType>

    suspend fun getById(id: Long): HomeType

    suspend fun updateForForm(dto: HomeTypeFormDto)

    suspend fun deleteForForm(id: Long): Int
}
