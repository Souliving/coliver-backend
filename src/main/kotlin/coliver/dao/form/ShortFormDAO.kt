package coliver.dao.form

import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto
import coliver.model.Form

interface ShortFormDAO {
    suspend fun getAll(): List<ShortFormDto>

    suspend fun getById(id: Long): ShortFormDto?

    suspend fun getForUser(userId: Long): List<ShortFormDto>

    suspend fun getWithFilter(
        userId: Long,
        filterDto: FilterDto
    ): List<ShortFormDto>

    suspend fun getWithFilterWithoutId(filterDto: FilterDto): List<ShortFormDto>

    suspend fun save(newForm: Form): Form

    suspend fun delete(id: Long): Int
}
