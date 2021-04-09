package pt.isel.daw.g08.project.utils

import java.net.URLDecoder
import java.net.URLEncoder

private const val urlCharset = "UTF-8"

fun String.urlEncode() = URLEncoder.encode(this, urlCharset)
fun String.urlDecode() = URLDecoder.decode(this, urlCharset)