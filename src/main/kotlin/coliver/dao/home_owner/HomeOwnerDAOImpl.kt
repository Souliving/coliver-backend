package coliver.dao.home_owner

import coliver.database.DatabaseFactory.dbQuery
import coliver.dto.CreateHomeOwnerDto
import coliver.model.HomeOwner
import coliver.model.HomeOwners
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class HomeOwnerDAOImpl : HomeOwnerDAO {
    private fun resultRowToHomeOwner(row: ResultRow) =
        HomeOwner(
            id = row[HomeOwners.id],
            metroId = row[HomeOwners.metroId],
            cityId = row[HomeOwners.cityId],
            homeTypeId = row[HomeOwners.homeTypeId],
            description = row[HomeOwners.description],
            photoId = row[HomeOwners.photoId],
        )

    override suspend fun getAll(): List<HomeOwner> =
        dbQuery {
            HomeOwners.selectAll().map(::resultRowToHomeOwner)
        }

    override suspend fun getById(id: Long): HomeOwner? =
        dbQuery {
            HomeOwners
                .selectAll()
                .where(HomeOwners.id.eq(id))
                .map(::resultRowToHomeOwner)
                .singleOrNull()
        }

    override suspend fun getByHomeTypeId(id: Long): List<HomeOwner> =
        dbQuery {
            HomeOwners.selectAll().where(HomeOwners.homeTypeId.eq(id)).map(::resultRowToHomeOwner)
        }

    override suspend fun insert(homeOwner: CreateHomeOwnerDto): Long =
        dbQuery {
            val ins =
                HomeOwners.insert {
                    it[metroId] = homeOwner.metroId!!
                    it[cityId] = homeOwner.cityId!!
                    it[homeTypeId] = homeOwner.homeTypeId!!
                    it[description] = homeOwner.description
                    it[photoId] = homeOwner.photoId!!
                }
            ins.resultedValues?.singleOrNull()?.let { resultRowToHomeOwner(it).id }!!
        }
}
