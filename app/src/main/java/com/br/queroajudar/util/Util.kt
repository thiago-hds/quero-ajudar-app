package com.br.queroajudar.util

object Constants {
    const val VIEW_TYPE_ITEM    = 1
    const val VIEW_TYPE_LOADING = 2
    const val PAGE_SIZE         = 10
}

fun convertOrganizationNameToFormatted(name : String) : String{
    //TODO implementar funcao
    return name
}

////TODO entender e refatorar
//fun hexStringToByteArray(s: String): String? {
////    val len = s.length
////    val data = ByteArray(len / 2)
////    var i = 0
////    while (i < len) {
////        data[i / 2] = ((Character.digit(s[i], 16) shl 4)
////                + Character.digit(s[i + 1], 16)).toByte()
////        i += 2
////    }
//    val s1 = s.toByteArray()
//
//    //return data.toString(Charsets.UTF_8)
//}

