package coliver.core


import java.util.*

fun getImgName(format: String): String {
    return "${generateRandomString(30)}.$format"
}

fun generateRandomString(
    length: Int = 20,
    source: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
): String {
    val random = Random()
    val charArray = CharArray(length) { source[random.nextInt(source.length)] }
    return String(charArray)
}
