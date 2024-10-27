package coliver.services

import coliver.dao.home_owner.HomeOwnerDAO
import coliver.model.HomeOwner

class HomeOwnerService(private val homeOwners: HomeOwnerDAO) {
    suspend fun getAll(): List<HomeOwner> = homeOwners.getAll()
    suspend fun getById(id: Long): HomeOwner = homeOwners.getById(id)!!
    suspend fun getByHomeTypes(id: Long): List<HomeOwner> = homeOwners.getByHomeTypeId(id)
}
