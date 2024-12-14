package coliver.dao.form

import coliver.dto.form.ShortFormDto

interface FavFormDAO {
    suspend fun getByUserId(userId: Long): List<ShortFormDto>

    suspend fun add(
        userId: Long,
        favFormId: Long
    )

    suspend fun delete(
        userId: Long,
        favFormId: Long
    )

    suspend fun deleteForForm(formId: Long)
}
