package coliver.dao.metro

import coliver.dto.MetroFormDto
import coliver.model.Metro

interface MetroDAO {
    suspend fun getAll(): List<Metro>

    suspend fun getMetro(metroId: Long): Metro

    suspend fun getMetroByCityId(cityId: Long): List<Metro>

    suspend fun getMetroByName(metroName: String): Metro

    suspend fun updateMetrosInForm(dto: MetroFormDto)

    suspend fun deleteMetroForForm(id: Long): Int
}
