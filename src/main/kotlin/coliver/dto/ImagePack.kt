package coliver.dto

data class ImagePack(
    var bucketName: String,
    var objectName: String,
    var imgLink: String? = null,
)
