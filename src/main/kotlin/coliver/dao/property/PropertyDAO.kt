package coliver.dao.property

import coliver.model.Property

interface PropertyDAO {
    suspend fun getById(id: Long): Property
}
