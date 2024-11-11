package coliver.plugins

import coliver.dao.city.CityDAO
import coliver.dao.city.CityDAOImpl
import coliver.dao.form.FavFormDAO
import coliver.dao.form.FormDAO
import coliver.dao.form.ShortFormDAO
import coliver.dao.form.impl.FavFormDAOImpl
import coliver.dao.form.impl.FormDAOImpl
import coliver.dao.form.impl.ShortFormDAOImpl
import coliver.dao.home_owner.HomeOwnerDAO
import coliver.dao.home_owner.HomeOwnerDAOImpl
import coliver.dao.home_type.HomeTypeDAO
import coliver.dao.home_type.HomeTypeDAOImpl
import coliver.dao.metro.MetroDAO
import coliver.dao.metro.MetroDAOImpl
import coliver.dao.property.PropertyDAO
import coliver.dao.property.PropertyDAOImpl
import coliver.dao.users.UserDAO
import coliver.dao.users.UserDAOImpl
import coliver.services.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

val metroModule = module {
    single<MetroDAO> { MetroDAOImpl() }
    single { MetroService(get()) }
}

val cityModule = module {
    single<CityDAO> { CityDAOImpl() }
    single { CityService(get()) }
}

val homeTypeModule = module {
    single<HomeTypeDAO> { HomeTypeDAOImpl() }
    single { HomeTypeService(get()) }
}

val homeOwnerModule = module {
    single<HomeOwnerDAO> { HomeOwnerDAOImpl() }
    single { HomeOwnerService(get()) }
}

val propertyModule = module {
    single<PropertyDAO> { PropertyDAOImpl() }
    single { PropertyService(get()) }
}

val userModule = module {
    single<UserDAO> { UserDAOImpl() }
    single { UserService(get()) }
}

val formModule = module {
    single<FormDAO> { FormDAOImpl() }
    single { FormService(get()) }
}

val shortFormModule = module {
    single<ShortFormDAO> { ShortFormDAOImpl() }
    single<ImageService> { ImageService() }
    single<ShortFormService> { ShortFormService(get(), get(), get(), get(), get(), get()) }
}

val favFormModule = module {
    single<FavFormDAO> { FavFormDAOImpl() }
    single<FavFormService> { FavFormService(get()) }
}

fun Application.configureKoin() {
    install(Koin) {
        modules(
            listOf(
                metroModule,
                cityModule,
                homeTypeModule,
                homeOwnerModule,
                propertyModule,
                userModule,
                formModule,
                shortFormModule,
                favFormModule
            )
        )
    }
}
