package coliver.services

import coliver.dao.form.ShortFormDAO
import coliver.database.Loom
import coliver.dto.HomeTypeFormDto
import coliver.dto.MetroFormDto
import coliver.dto.form.CreateFormDto
import coliver.dto.form.FilterDto
import coliver.dto.form.ShortFormDto
import coliver.model.Form
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.toKotlinLocalDateTime

class ShortFormService(
    private val shortFormDAO: ShortFormDAO,
    private val propertyService: PropertyService,
    private val metroService: MetroService,
    private val homeTypesService: HomeTypeService,
    private val favFormService: FavFormService,
    private val imageService: ImageService
) {

    suspend fun getAll(): List<ShortFormDto> {
        val allForms = shortFormDAO.getAll()

//        val ids = allForms.map { it.photoId ?: 0 }
//        val imgLinks = imageService.getLinksForIds(ids)

        allForms.pmap {
            it.imageLink = imageService.getImageLinkById(it.photoId?: 0)
        }

        return allForms
    }

    suspend fun getById(id: Long): ShortFormDto = shortFormDAO.getById(id)!!.apply {
        this.imageLink = imageService.getImageLinkById(this.photoId!!)
    }

    suspend fun getForUser(userId: Long): List<ShortFormDto> {
        val forms = shortFormDAO.getForUser(userId)

        val ids = forms.map { it.photoId ?: 0 }
        val imgLinks = imageService.getLinksForIds(ids)

        forms.map { it ->
            it.imageLink = imgLinks[it.photoId]
        }
        return forms
    }

    suspend fun getWithFilter(userId: Long, filter: FilterDto): List<ShortFormDto> {
        val filteredDao = shortFormDAO.getWithFilter(userId, filter)

        val ids = filteredDao.map { it.photoId ?: 0 }
        val imgLinks = imageService.getLinksForIds(ids)

        filteredDao.map { it ->
            it.imageLink = imgLinks[it.photoId]
        }

        return filteredDao
    }

    suspend fun getWithFilterWithoutId(filter: FilterDto): List<ShortFormDto> {
        val filteredDao = shortFormDAO.getWithFilterWithoutId(filter)

        val ids = filteredDao.map { it.photoId ?: 0 }
        val imgLinks = imageService.getLinksForIds(ids)

        filteredDao.map { it ->
            it.imageLink = imgLinks[it.photoId]
        }

        return filteredDao
    }

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

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): Iterable<B> = coroutineScope {
    map { async(Dispatchers.Loom) { f(it) } }.awaitAll()
}
