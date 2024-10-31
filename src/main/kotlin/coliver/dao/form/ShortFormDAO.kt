package coliver.dao.form

import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto

interface ShortFormDAO {
    suspend fun getAll(): List<ShortFormDto>
    suspend fun getById(id: Long): ShortFormDto?
    suspend fun getForUser(userId: Long): List<ShortFormDto>
    suspend fun getWithFilter(userId: Long, filter: FilterDto): List<ShortFormDto>
}
