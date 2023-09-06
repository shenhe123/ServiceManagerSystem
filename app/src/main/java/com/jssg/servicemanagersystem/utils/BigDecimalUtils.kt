package com.jssg.servicemanagersystem.utils

import java.math.BigDecimal

/**
 * bitmax-android
 * Created by he.shen on 2023/4/17.
 */
object BigDecimalUtils {

    fun String?.bigDecimal(): BigDecimal {
        return if (this.isNullOrEmpty() || this.equals("--", false) || this.equals(
                "- -",
                false
            )
        ) BigDecimal.ZERO
        else this.toBigDecimal()
    }
    fun String?.stripZeros(): String {
        return this.bigDecimal().stripTrailingZeros().toPlainString()
    }

    fun String?.abs(): String {
        return this.bigDecimal().abs().stripTrailingZeros().toPlainString()
    }

    fun Int?.bigDecimal(): BigDecimal {
        return this?.toBigDecimal() ?: BigDecimal.ZERO
    }

    fun Double?.bigDecimal(): BigDecimal {
        return this?.toBigDecimal() ?: BigDecimal.ZERO
    }

    fun Long?.bigDecimal(): BigDecimal {
        return this?.toBigDecimal() ?: BigDecimal.ZERO
    }

    fun Float?.bigDecimal(): BigDecimal {
        return this?.toBigDecimal() ?: BigDecimal.ZERO
    }
}