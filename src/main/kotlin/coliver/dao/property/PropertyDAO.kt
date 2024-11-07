package coliver.dao.property

import coliver.dto.CreatePropertiesDto
import coliver.model.Property

interface PropertyDAO {
    suspend fun getById(id: Long): Property
    suspend fun create(dto: CreatePropertiesDto): Long
    suspend fun delete(id: Long): Int
}
