package coliver.dao.form

import coliver.dto.form.FormDto
import coliver.dto.form.FullFormDto
import coliver.dto.form.ShortFormDto

interface FormDAO {
    suspend fun getAll(): List<FormDto>

    suspend fun getById(id: Long): List<FormDto>

    suspend fun getByUserId(userId: Long): List<FormDto>

    suspend fun getFullFormById(id: Long): List<FullFormDto>

    suspend fun create(
        userId: Long,
        form: FormDto
    ): ShortFormDto
}
