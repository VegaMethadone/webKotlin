package org.example.app

import org.example.service.BookService
import javax.xml.ws.Endpoint


object App {
    @JvmStatic
    fun main(args: Array<String>)  {
        val url = "http://localhost:8080/BookService"
        println("http://localhost:8080/BookService?wsdl")
        Endpoint.publish(url, BookService())
    }
}