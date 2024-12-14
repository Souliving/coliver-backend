package coliver.dao.city

import coliver.database.DatabaseFactory.dbQuery
import coliver.model.Cities
import coliver.model.City
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class CityDAOImpl : CityDAO {
    private fun resultRowToCity(row: ResultRow) =
        City(
            id = row[Cities.id],
            name = row[Cities.name],
        )

    override suspend fun getAll(): List<City> =
        dbQuery {
            Cities.selectAll().map(::resultRowToCity)
        }
}
