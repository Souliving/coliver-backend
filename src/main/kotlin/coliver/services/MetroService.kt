package coliver.services

import coliver.dao.metro.MetroDAO
import coliver.dto.MetroFormDto
import coliver.model.Metro

class MetroService(private val metroDAO: MetroDAO) {
    suspend fun all(): List<Metro> =
        metroDAO.getAll()

    suspend fun getMetroById(metroId: Long) = metroDAO.getMetro(metroId)

    suspend fun getMetroByCityId(cityId: Long) = metroDAO.getMetroByCityId(cityId)

    suspend fun getMetroByName(name: String) = metroDAO.getMetroByName(name)

    suspend fun updateMetrosInForm(dto: MetroFormDto) = metroDAO.updateMetrosInForm(dto)

    suspend fun deleteMetroForForm(id: Long)= metroDAO.deleteMetroForForm(id)
}
