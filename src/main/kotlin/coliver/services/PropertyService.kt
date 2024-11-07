package coliver.services

import coliver.dao.property.PropertyDAO
import coliver.dto.CreatePropertiesDto
import coliver.model.Property

class PropertyService(private val propertyDAO: PropertyDAO) {
    suspend fun getById(id: Long): Property = propertyDAO.getById(id)
    suspend fun create(dto: CreatePropertiesDto): Long = propertyDAO.create(dto)
    suspend fun delete(id: Long) = propertyDAO.delete(id)
}
