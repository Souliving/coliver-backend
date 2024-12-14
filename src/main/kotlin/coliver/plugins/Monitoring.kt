package coliver.plugins

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.micrometer.core.instrument.binder.jvm.*
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import java.time.Duration

val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

fun Application.configureMonitoring() {
    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
        distributionStatisticConfig =
            DistributionStatisticConfig
                .Builder()
                .percentilesHistogram(true)
                .maximumExpectedValue(Duration.ofSeconds(20).toNanos().toDouble())
                .serviceLevelObjectives(
                    Duration.ofMillis(100).toNanos().toDouble(),
                    Duration.ofMillis(500).toNanos().toDouble(),
                ).build()
        meterBinders =
            listOf(
                ClassLoaderMetrics(),
                JvmMemoryMetrics(),
                JvmGcMetrics(),
                ProcessorMetrics(),
                JvmThreadMetrics(),
                FileDescriptorMetrics(),
                UptimeMetrics(),
                JvmInfoMetrics(),
            )
    }
}
