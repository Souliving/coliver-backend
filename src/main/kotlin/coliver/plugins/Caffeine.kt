package coliver.plugins

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.ktor.server.application.Application
import java.util.concurrent.TimeUnit


val imgCaffeine: Cache<Long, String?> = Caffeine.newBuilder()
    .expireAfterWrite(1, TimeUnit.HOURS)
    .maximumSize(1024)
    .build<Long, String>()

