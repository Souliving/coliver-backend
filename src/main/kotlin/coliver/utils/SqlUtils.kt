package coliver.utils

import coliver.dto.form.FilterDto
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.ResultSet

fun <T : Any> String.execAndMap(transform: (ResultSet) -> T): List<T> {
    val result = arrayListOf<T>()
    TransactionManager.current().exec(this) { rs ->
        while (rs.next()) {
            result += transform(rs)
        }
    }
    return result
}

fun String.exec(): Unit? {
    return TransactionManager.current().exec(this)
}

fun buildFilterRequest(userId: Long, filter: FilterDto): String {
    val sqlString = "select * from" +
        " get_short_forms_with_filter" +
        "(${userId.buildSqlParameter()}," +
        "${filter.price.startPrice?.buildSqlParameter()},${filter.price.endPrice?.buildSqlParameter()}," +
        "${filter.age.startAge?.buildSqlParameter()}, ${filter.age.endAge?.buildSqlParameter()}," +
        "${filter.cityId.buildSqlParameter()}, ${filter.metroIds.buildSqlParameter()}," +
        " ${filter.smoking.buildSqlParameter()}, ${filter.alcohol.buildSqlParameter()}," +
        " ${filter.petFriendly.buildSqlParameter()}, ${filter.isClean.buildSqlParameter()})"
    return sqlString
}

private fun Number.buildSqlParameter(): String {
    if (this == null) {
        return "null"
    }
    return when (this) {
        is Long -> "$this::bigint"
        is Int -> "$this::int"
        else -> "$this::int"
    }
}

private fun List<Number>.buildSqlParameter(): String {
    if (this.isEmpty()) {
        return "null"
    }
    var result = "array["
    this.forEach {
        result += when (it) {
            is Int -> "$it::int,"
            is Long -> "$it::bigint,"
            else -> "$it::int,"
        }
    }
    result = result.substring(0, result.length - 1)
    result = result.plus("]")
    return result
}

private fun Boolean?.buildSqlParameter(): String {
    if (this == null)
        return "null"
    return this.toString()
}
