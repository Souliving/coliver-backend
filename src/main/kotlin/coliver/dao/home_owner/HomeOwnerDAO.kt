package coliver.dao.home_owner

import coliver.dto.CreateHomeOwnerDto
import coliver.model.HomeOwner

interface HomeOwnerDAO {
    suspend fun getAll(): List<HomeOwner>

    suspend fun getById(id: Long): HomeOwner?

    suspend fun getByHomeTypeId(id: Long): List<HomeOwner>

    suspend fun insert(homeOwner: CreateHomeOwnerDto): Long
}
