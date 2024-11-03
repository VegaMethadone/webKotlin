package org.example.service

import javax.xml.ws.WebFault


@WebFault(name = "IllegalBookFieldException", faultBean = "org.example.service.BookServiceFault")
class IllegalBookFieldException(
    message: String,
    private  val fault: BookServiceFault,
    cause: Throwable? = null
) : Exception(message, cause) {
    fun getFaultInfo(): BookServiceFault = fault
}

//@WebFault(name = "BookNotFoundException", faultBean = "org.example.service.BookServiceFault")
//class BookNotFoundException(
//    message: String,
//    private val fault: BookServiceFault,
//    cause: Throwable? = null
//) : Exception(message, cause) {
//    fun getFaultInfo(): BookServiceFault = fault
//}