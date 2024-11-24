package coliver.plugins

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Minio(private var minioClient: MinioClient) {

    suspend fun getPresignedUrl(bucketName: String, objectName: String): String? = withContext(Dispatchers.IO) {
        minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).`object`(objectName)
                .expiry(60 * 60 * 24).build()
        )
    }

}

val minio = Minio(
    minioClient =
    MinioClient.builder()
//        .httpClient(httpClient)
        .endpoint("https://minio.coliver.tech:9000")
        .credentials("xw8kl9vZcAeSVffNZ9TD", "sa7IqQ8D0yQhiZVwnz3TUQeXtMBEubGNXfdUIdvV")
        .build()
)



