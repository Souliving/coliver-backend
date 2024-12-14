package coliver.services

import coliver.dao.city.CityDAO
import coliver.model.City

class CityService(
    private val cityDAO: CityDAO
) {
    private val cities =
        listOf(
            City(1, "Москва"),
            City(2, "Санкт-Петербург"),
        )

    fun getAll(): List<City> = cities

    fun getCityById(id: Long): City = cities.single { it.id == id }
}
