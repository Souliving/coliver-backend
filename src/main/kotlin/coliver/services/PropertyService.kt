package coliver.services

import coliver.dao.property.PropertyDAO
import coliver.model.Property

class PropertyService(private val propertyDAO: PropertyDAO) {
    suspend fun getById(id: Long): Property = propertyDAO.getById(id)
}
