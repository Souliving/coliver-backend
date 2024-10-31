package coliver.services

import coliver.dao.form.ShortFormDAO
import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto

class ShortFormService(private val shortFormDAO: ShortFormDAO) {
    suspend fun getAll(): List<ShortFormDto> = shortFormDAO.getAll()
    suspend fun getById(id: Long): ShortFormDto = shortFormDAO.getById(id)!!
    suspend fun getForUser(userId: Long): List<ShortFormDto> = shortFormDAO.getForUser(userId)
    suspend fun getWithFilter(userId: Long, filter: FilterDto): List<ShortFormDto> =
        shortFormDAO.getWithFilter(userId, filter)
}
