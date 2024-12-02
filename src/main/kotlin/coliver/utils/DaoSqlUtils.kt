package coliver.utils

import coliver.dto.form.ShortFormDto
import coliver.model.Cities
import coliver.model.City
import coliver.model.FavoriteForms
import coliver.model.Form
import coliver.model.FormMetroIds
import coliver.model.Forms
import coliver.model.Metro
import coliver.model.Metros
import coliver.model.Properties
import coliver.model.Property
import coliver.model.Users
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.VarCharColumnType
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.castTo
import org.jetbrains.exposed.sql.groupConcat


val metroIds = Metros.id.castTo(VarCharColumnType())
val metroCityIds = Metros.cityId.castTo(VarCharColumnType())

val metroIdsAgg = metroIds.groupConcat(",", distinct = true)
val metroNamesAgg = Metros.name.groupConcat(",", distinct = true)
val metroCityIdsAgg = metroCityIds.groupConcat(",", distinct = true)

private fun shortFormsJoin() = Forms.join(
    Users, joinType = JoinType.INNER, onColumn =
    Forms.userId, otherColumn = Users.id
)
    .join(Properties, JoinType.INNER, onColumn = Forms.propertiesId, otherColumn = Properties.id)
    .join(Cities, JoinType.INNER, onColumn = Forms.cityId, otherColumn = Cities.id)
    .join(FormMetroIds, JoinType.LEFT, onColumn = Forms.id, otherColumn = FormMetroIds.formId)
    .join(Metros, JoinType.LEFT, onColumn = Metros.id, otherColumn = FormMetroIds.metroId)

fun aggregateMetros(ids: String, names: String, cityId: String): List<Metro> {
    val listOfMetros = mutableListOf<Metro>()

    val metroIds = ids.split(',')
    val metroNames = names.split(',')
    val cityIds = cityId.split(',')

    metroIds.forEachIndexed { index, s ->
        listOfMetros.add(Metro(s.toLong(), metroNames[index], cityIds.first().toLong()))
    }

    return listOfMetros
}

fun resultRowToShortFormWithFavs(row: ResultRow): ShortFormDto {
    return ShortFormDto(
        id = row[Forms.id],
        name = row[Users.name],
        age = row[Users.age],
        city = City(row[Cities.id], row[Cities.name]),
        metro = aggregateMetros(row[metroIdsAgg], row[metroNamesAgg], row[metroCityIdsAgg]),
        budget = row[Forms.budget],
        description = row[Forms.description],
        properties = Property(
            id = row[Properties.id],
            smoking = row[Properties.smoking],
            alcohol = row[Properties.alcohol],
            petFriendly = row[Properties.petFriendly],
            isClean = row[Properties.isClean],
            homeOwnerId = row[Properties.homeOwnerId],
        ),
        photoId = row[Forms.photoId],
        dateMove = row[Forms.dateMove],
        onlineDateTime = row[Forms.onlineDateTime],
        isFavorite = row[FavoriteForms.favFormId] != null,
    )
}

fun resultRowToShortForm(row: ResultRow): ShortFormDto {
    return ShortFormDto(
        id = row[Forms.id],
        name = row[Users.name],
        age = row[Users.age],
        city = City(row[Cities.id], row[Cities.name]),
        metro = aggregateMetros(row[metroIdsAgg], row[metroNamesAgg], row[metroCityIdsAgg]),
        budget = row[Forms.budget],
        description = row[Forms.description],
        properties = Property(
            id = row[Properties.id],
            smoking = row[Properties.smoking],
            alcohol = row[Properties.alcohol],
            petFriendly = row[Properties.petFriendly],
            isClean = row[Properties.isClean],
            homeOwnerId = row[Properties.homeOwnerId],
        ),
        photoId = row[Forms.photoId],
        dateMove = row[Forms.dateMove],
        onlineDateTime = row[Forms.onlineDateTime],
        isFavorite = false
    )
}

fun getFavShortForms(userId: Long) = shortFormsJoin()
    .join(FavoriteForms, JoinType.INNER, onColumn = FavoriteForms.favFormId, otherColumn = Forms.id,
        additionalConstraint = { FavoriteForms.userId eq userId })
    .select(
        Forms.id,
        Users.name,
        Users.age,
        Cities.id,
        Cities.name,
        Forms.budget,
        Forms.description,
        Properties.id,
        Properties.alcohol,
        Properties.petFriendly,
        Properties.smoking,
        Properties.isClean,
        Properties.homeOwnerId,
        Forms.photoId,
        Forms.dateMove,
        Forms.onlineDateTime,
        metroIdsAgg,
        metroNamesAgg,
        metroCityIdsAgg,
        FavoriteForms.favFormId
    )
    .groupBy(
        Forms.id, Users.id, Users.name, Users.age, Cities.id, Cities.name, Forms.budget,
        Forms.description, Forms.dateMove, Properties.id, Properties.smoking, Properties.alcohol,
        Properties.petFriendly, Properties.isClean, Properties.homeOwnerId, Forms.photoId,
        Forms.onlineDateTime, FavoriteForms.favFormId
    )

fun resultRowToFormDto(row: ResultRow): Form = Form(
    id = row[Forms.id],
    userId = row[Forms.userId],
    description = row[Forms.description],
    reviews = listOf(row[Forms.reviews]),
    photoId = row[Forms.photoId],
    propertiesId = row[Forms.propertiesId],
    cityId = row[Forms.cityId],
    budget = row[Forms.budget],
    dateMove = row[Forms.dateMove],
    onlineDateTime = row[Forms.onlineDateTime],
    rating = row[Forms.rating]
)

fun getShortForms() =
    shortFormsJoin().select(
        Forms.id,
        Users.name,
        Users.age,
        Cities.id,
        Cities.name,
        Forms.budget,
        Forms.description,
        Properties.id,
        Properties.alcohol,
        Properties.petFriendly,
        Properties.smoking,
        Properties.isClean,
        Properties.homeOwnerId,
        Forms.photoId,
        Forms.dateMove,
        Forms.onlineDateTime,
        metroIdsAgg,
        metroNamesAgg,
        metroCityIdsAgg
    )
        .groupBy(
            Forms.id, Users.id, Users.name, Users.age, Cities.id, Cities.name, Forms.budget,
            Forms.description, Forms.dateMove, Properties.id, Properties.smoking, Properties.alcohol,
            Properties.petFriendly, Properties.isClean, Properties.homeOwnerId, Forms.photoId,
            Forms.onlineDateTime
        )

fun getShortFormsWithFav(userId: Long) = shortFormsJoin()
    .join(
        FavoriteForms,
        JoinType.LEFT,
        onColumn = FavoriteForms.favFormId,
        otherColumn = Forms.id,
        additionalConstraint = { FavoriteForms.userId eq userId })
    .select(
        Forms.id,
        Users.name,
        Users.age,
        Cities.id,
        Cities.name,
        Forms.budget,
        Forms.description,
        Properties.id,
        Properties.alcohol,
        Properties.petFriendly,
        Properties.smoking,
        Properties.isClean,
        Properties.homeOwnerId,
        Forms.photoId,
        Forms.dateMove,
        Forms.onlineDateTime,
        metroIdsAgg,
        metroNamesAgg,
        metroCityIdsAgg,
        FavoriteForms.favFormId
    )
    .groupBy(
        Forms.id, Users.id, Users.name, Users.age, Cities.id, Cities.name, Forms.budget,
        Forms.description, Forms.dateMove, Properties.id, Properties.smoking, Properties.alcohol,
        Properties.petFriendly, Properties.isClean, Properties.homeOwnerId, Forms.photoId,
        Forms.onlineDateTime, FavoriteForms.favFormId
    )
    .andWhere { Users.id neq userId }

fun filterSQL() = shortFormsJoin()
    .select(
        Forms.id,
    )
    .groupBy(
        Forms.id, Users.id, Users.name, Users.age, Cities.id, Cities.name, Forms.budget,
        Forms.description, Forms.dateMove, Properties.id, Properties.smoking, Properties.alcohol,
        Properties.petFriendly, Properties.isClean, Properties.homeOwnerId, Forms.photoId,
        Forms.onlineDateTime
    )
