package coliver.dao.city

import coliver.model.City

interface CityDAO {
    suspend fun getAll(): List<City>
}
