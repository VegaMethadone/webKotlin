package org.example.service

import com.sun.xml.ws.fault.SOAPFaultBuilder
import org.example.entity.book.Book
import org.example.postgreSQLDAO.PostgreSQLDAO
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebService

@WebService(serviceName = "BookService")
open class BookService {
    var soap: SOAPFaultBuilder? = null

    @WebMethod(operationName = "getAll")
    open fun getAll(): List<Book> {
        val dao = PostgreSQLDAO()

        return dao.all
    }

    @WebMethod(operationName = "getBookByValues")
    open fun getBookByValues(
        @WebParam(name = "author") author: String? = null,
        @WebParam(name = "title") title: String? = null,
        @WebParam(name = "description") description: String? = null,
        @WebParam(name = "published") published: Int = 0,
        @WebParam(name = "score") score: Int = 0
    ): List<Book> {
        val dao =  PostgreSQLDAO()

        return  dao.getBookByValues(author, title, description, published, score)
    }

}