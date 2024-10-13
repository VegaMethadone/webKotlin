package org.example.client

import org.example.entity.book.Book
import org.example.service.BookService
import java.net.MalformedURLException
import java.net.URL


object Client {
    @Throws(MalformedURLException::class)
    @JvmStatic

    fun  main(args: Array<String>) {
        val url = URL("http://localhost:8080/BookService?wsdl")
        val bookService: BookService = BookService()

        val books: List<Book> = bookService.getAll()
        println("All books")

        for (book in books) {
           println("Book {" +
                   "ID: " + book.id.toString() +
                   "AUTHOR: " +  book.author +
                   "TITLE: " + book.title +
                   "DESCRIPTION: " + book.description  +
                   "PUBLISHED: " + book.published.toString()  +
                   "SCORE: " + book.score.toString() + "}")
        }



        val author = "Александр Блок"
        val title  = "Двенадцать"
        val booksByValue: List<Book> = bookService.getBookByValues(author, title)

        for (book in booksByValue) {
            println("ID: " + book.id)
            println("AUTHOR: " + book.author)
            println("TITLE: " + book.title)
            println("DESCRIPTION: " + book.description)
            println("PUBLISHED: " + book.published.toString())
            println("SCORE: " + book.score.toString())
            println("------------")
        }
    }
}