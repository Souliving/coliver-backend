package coliver.services

import coliver.dao.form.FavFormDAO
import coliver.dto.form.ShortFormDto

class FavFormService(private val favFormDAO: FavFormDAO) {
    suspend fun getFavForms(userId: Long): List<ShortFormDto> = favFormDAO.getByUserId(userId)
    suspend fun add(userId: Long, formId: Long) = favFormDAO.add(userId, formId)
    suspend fun delete(userId: Long, formId: Long) = favFormDAO.delete(userId, formId)
}
