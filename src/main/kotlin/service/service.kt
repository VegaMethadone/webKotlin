package org.example.service

import com.sun.xml.ws.fault.SOAPFaultBuilder
import org.example.entity.book.Book
import org.example.postgreSQLDAO.PostgreSQLDAO
import org.postgresql.util.PSQLException
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


    @WebMethod(operationName = "addBook")
    open fun addBook(
        @WebParam(name = "author") author: String? = null,
        @WebParam(name = "title") title: String? = null,
        @WebParam(name = "description") description: String? = null,
        @WebParam(name = "published") published: Int = 0,
        @WebParam(name = "score") score: Int = 0
    ):  Int {
        val fault  = BookServiceFault.invalidFieldInstance()
        if (author.isNullOrEmpty()) {
            throw IllegalBookFieldException(
                "Author field cannot be null or blank.",
                fault
            )
        }else if (title.isNullOrEmpty()) {
            throw  IllegalBookFieldException(
                "Title field cannot be null or blank.",
                fault
            )
        }else if (description.isNullOrEmpty()) {
            throw  IllegalBookFieldException(
                "Description field cannot be null or blank.",
                fault
            )
        }else if (published <= 0) {
            throw  IllegalBookFieldException(
                "Published field cannot be less or equal to zero.",
                fault
            )
        }else if (score < 0 || score  >  10) {
            throw  IllegalBookFieldException(
                "Score field cannot be less than zero or bigger than ten.",
                fault
            )

        }
        else {
            val dao = PostgreSQLDAO()

            return  dao.addBook(author, title, description, published, score)
        }

    }

    @WebMethod(operationName = "updateBook")
    open fun updateBook(
        @WebParam(name = "id") id: Int,
        @WebParam(name = "author") author: String? = null,
        @WebParam(name = "title") title: String? = null,
        @WebParam(name = "description") description: String? = null,
        @WebParam(name = "published") published: Int? = 0,
        @WebParam(name = "score") score: Int? = 0
    ): Boolean {

        val dao = PostgreSQLDAO()

        val res =   dao.updateBook(id, author, title, description, published, score)
        if (!res) {
            val notFound = BookServiceFault.notFoundInstance()
            throw  IllegalBookFieldException(
                "Could not find",
                notFound
            )
        }

        return  true
    }

    @WebMethod(operationName = "deleteBook")
    open fun deleteBook(
        @WebParam(name = "id") id: Int
    ): Boolean {

        val  notFound  = BookServiceFault.notFoundInstance()

        val dao = PostgreSQLDAO()

        val res = dao.deleteBook(id)
        if  (!res) {
            throw  IllegalBookFieldException(
                "Could not delete",
                notFound
            )
        }

        return  true
    }
}