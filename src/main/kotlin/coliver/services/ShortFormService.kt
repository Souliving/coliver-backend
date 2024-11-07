package coliver.services

import coliver.dao.form.ShortFormDAO
import coliver.dto.HomeTypeFormDto
import coliver.dto.MetroFormDto
import coliver.dto.form.CreateFormDto
import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto
import coliver.model.Form
import kotlinx.datetime.toKotlinLocalDateTime

class ShortFormService(
    private val shortFormDAO: ShortFormDAO,
    private val propertyService: PropertyService,
    private val metroService: MetroService,
    private val homeTypesService: HomeTypeService,
    private val favFormService: FavFormService
) {

    suspend fun getAll(): List<ShortFormDto> = shortFormDAO.getAll()
    suspend fun getById(id: Long): ShortFormDto = shortFormDAO.getById(id)!!
    suspend fun getForUser(userId: Long): List<ShortFormDto> = shortFormDAO.getForUser(userId)
    suspend fun getWithFilter(userId: Long, filter: FilterDto): List<ShortFormDto> =
        shortFormDAO.getWithFilter(userId, filter)

    suspend fun createForm(dto: CreateFormDto): ShortFormDto {
        val newForm = shortFormDAO.save(dto.toForm())

        metroService.updateMetrosInForm(MetroFormDto(newForm.id!!, dto.metroIds!!))
        homeTypesService.updateForForm(HomeTypeFormDto(newForm.id, dto.homeTypesIds!!))
        return shortFormDAO.getById(newForm.id)!!
    }

    suspend fun deleteForm(id: Long) {
        val form = shortFormDAO.getById(id)!!
        metroService.deleteMetroForForm(id)
        homeTypesService.deleteForForm(id)
        propertyService.delete(form.properties!!.id!!)
        favFormService.deleteForFormId(id)
        shortFormDAO.delete(id)
    }

    private suspend fun CreateFormDto.toForm() = Form(
        userId = this.userId,
        description = this.description,
        rating = this.rating,
        reviews = this.reviews,
        photoId = this.photoId,
        propertiesId = propertyService.create(this.properties),
        cityId = this.cityId,
        budget = this.budget,
        dateMove = this.dateMove.toKotlinLocalDateTime(),
        onlineDateTime = this.onlineDateTime.toKotlinLocalDateTime()
    )
}
