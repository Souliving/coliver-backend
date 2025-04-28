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

    suspend fun getFullFormById(id: Long): List<FullFormDto> {
        val forms = formDAO.getFullFormById(id)
        loadImages(forms)
        return forms
    }

    private suspend fun loadImages(listDto: List<FullFormDto>) {
        val ids = listDto.map { it.photoId ?: 0 }
        val imgPack = imageService.getImageLinkPack(ids)

        listDto.pmap {
            it.imageLink = imgPack[it.photoId]?.imgLink ?: "not null"
        }
    }
}
