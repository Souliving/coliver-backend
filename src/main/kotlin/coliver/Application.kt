package coliver

import coliver.dao.city.CityDAO
import coliver.dao.city.CityDAOImpl
import coliver.dao.home_owner.HomeOwnerDAO
import coliver.dao.home_owner.HomeOwnerDAOImpl
import coliver.dao.home_type.HomeTypeDAO
import coliver.dao.home_type.HomeTypeDAOImpl
import coliver.dao.metro.MetroDAO
import coliver.dao.metro.MetroDAOImpl
import coliver.plugins.configureDatabases
import coliver.plugins.configureRouting
import coliver.plugins.configureSerialization
import coliver.services.CityService
import coliver.services.HomeOwnerService
import coliver.services.HomeTypeService
import coliver.services.MetroService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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

fun Application.module() {
    install(Koin) {
        modules(listOf(metroModule, cityModule, homeTypeModule, homeOwnerModule))
    }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
    configureSerialization()
    configureDatabases()
    configureRouting()
}
