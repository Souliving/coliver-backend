package coliver.services

import coliver.dao.form.FavFormDAO
import coliver.dto.form.ShortFormDto

class FavFormService(private val favFormDAO: FavFormDAO, private val imageService: ImageService) {
    suspend fun getFavForms(userId: Long): List<ShortFormDto> {
        val favForms = favFormDAO.getByUserId(userId)
        val ids = favForms.map { it.photoId ?: 0 }
        val imgLinks = imageService.getLinksForIds(ids)

        favForms.map { it ->
            it.imageLink = imgLinks[it.photoId]
        }
        return favForms
    }

    suspend fun add(userId: Long, formId: Long) = favFormDAO.add(userId, formId)
    suspend fun delete(userId: Long, formId: Long) = favFormDAO.delete(userId, formId)
    suspend fun deleteForFormId(formId: Long) = favFormDAO.deleteForForm(formId)
}
