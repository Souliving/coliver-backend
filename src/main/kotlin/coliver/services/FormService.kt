package coliver.services

import coliver.dao.form.FormDAO
import coliver.dto.form.FormDto
import coliver.dto.form.FullFormDto

class FormService(
    private val formDAO: FormDAO,
    private val imageService: ImageService
) {
    suspend fun getAll(): List<FormDto> = formDAO.getAll()

    suspend fun getById(id: Long): List<FormDto> = formDAO.getById(id)

    suspend fun getByUserId(userId: Long): List<FormDto> = formDAO.getByUserId(userId)

    suspend fun getFullFormById(id: Long): List<FullFormDto> = formDAO.getFullFormById(id).apply { 
        this.forEach { form->
            form.imageLink = imageService.getImageLinkById(form.photoId!!)
        }
    }
}
