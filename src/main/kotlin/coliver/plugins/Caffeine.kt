package coliver.plugins

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.asCache
import java.util.concurrent.TimeUnit

val imgCaffeine =
    Caffeine
        .newBuilder()
        .expireAfterWrite(1, TimeUnit.HOURS)
        .maximumSize(1024)
        .asCache<Long, String>()
