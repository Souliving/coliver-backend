package coliver.images

import coliver.database.Loom
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.ObjectWriteResponse
import io.minio.PutObjectArgs
import io.minio.http.Method
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Minio(
    private var minioClient: MinioClient
) {
    suspend fun getPresignedUrl(
        bucketName: String,
        objectName: String
    ): String? =
        withContext(Dispatchers.Loom) {
            minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                    .builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .`object`(objectName)
                    .expiry(60 * 60 * 24)
                    .build(),
            )
        }

    suspend fun putObject(
        bucketName: String,
        objectName: String,
        stream: InputStream,
        contentType: String = "image/jpg"
    ): ObjectWriteResponse =
        withContext(
            Dispatchers.IO,
        ) {
            minioClient.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .`object`(objectName)
                    .stream(stream, -1, 10485760)
                    .contentType(contentType)
                    .build(),
            )
        }
}

val okHttpClient =
    OkHttpClient()
        .newBuilder()
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .dispatcher(createDispatcher())
        .connectionPool(createConnectionPool())
        .build()

private fun createDispatcher(): Dispatcher {
    val dispatcher = Dispatcher(Executors.newCachedThreadPool())
    dispatcher.maxRequests = 30000
    dispatcher.maxRequestsPerHost = 10000
    return dispatcher
}

private fun createConnectionPool(): ConnectionPool = ConnectionPool(2048, 10000, TimeUnit.MILLISECONDS)

val minio =
    Minio(
        minioClient =
            MinioClient
                .builder()
                .httpClient(okHttpClient)
                .endpoint("https://minio.coliver.tech:9000")
                .credentials("xw8kl9vZcAeSVffNZ9TD", "sa7IqQ8D0yQhiZVwnz3TUQeXtMBEubGNXfdUIdvV")
                .build(),
    )
