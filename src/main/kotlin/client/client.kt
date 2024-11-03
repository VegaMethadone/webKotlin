package org.example.client

import org.example.entity.book.Book
import org.example.service.BookService
import org.example.service.IllegalBookFieldException
import java.net.MalformedURLException
import java.net.URL


object Client {
    @Throws(MalformedURLException::class)
    @JvmStatic

    fun  main(args: Array<String>) {
        val url = URL("http://localhost:8080/BookService?wsdl")
        val bookService: BookService = BookService()

        var books: List<Book> = bookService.getAll()
        println("All books")

        for (book in books) {
           println(" Book {" +
                   " ID: " + book.id.toString() +
                   " AUTHOR: " +  book.author +
                   " TITLE: " + book.title +
                   " DESCRIPTION: " + book.description  +
                   " PUBLISHED: " + book.published.toString()  +
                   " SCORE: " + book.score.toString() + "}")
        }

        books = emptyList()



        val author = "Александр Блок"
        val title  = "Двенадцать"
        val booksByValue: List<Book> = bookService.getBookByValues(author, title)

        for (book in booksByValue) {
            println(" ID: " + book.id)
            println(" AUTHOR: " + book.author)
            println(" TITLE: " + book.title)
            println(" DESCRIPTION: " + book.description)
            println(" PUBLISHED: " + book.published.toString())
            println(" SCORE: " + book.score.toString())
            println("------------")
        }


        println("Lab 2 TEST")

        println("TEST: addBook ->")
        val addedID: Int = bookService.addBook("Артем", "WTB", "Web Service guide", 2024, 8)
        if (addedID < 1)  {
            println("Failed to add book")
        }else {
            println("SUCCESS: added id -> $addedID")
        }

        books = bookService.getAll()
        println("All books")

        for (book in books) {
            println(" Book {" +
                    " ID: " + book.id.toString() +
                    " AUTHOR: " +  book.author +
                    " TITLE: " + book.title +
                    " DESCRIPTION: " + book.description  +
                    " PUBLISHED: " + book.published.toString()  +
                    " SCORE: " + book.score.toString() + "}")
        }

        books = emptyList()


        println("TEST: updateBook ->")
        var isSuccess: Boolean = bookService.updateBook(addedID, "Ilon Musk")
        if (isSuccess) {
            println("Book is successfully changed!")
        }else {
            println("Failed to change book")
        }

        books = bookService.getAll()
        println("All books")

        for (book in books) {
            println(" Book {" +
                    " ID: " + book.id.toString() +
                    " AUTHOR: " +  book.author +
                    " TITLE: " + book.title +
                    " DESCRIPTION: " + book.description  +
                    " PUBLISHED: " + book.published.toString()  +
                    " SCORE: " + book.score.toString() + "}")
        }

        books = emptyList()

        println("TEST: deleteBook ->")
        isSuccess = bookService.deleteBook(addedID)
        if (isSuccess) {
            println("Book is successfully deleted!")
        }else {
            println("Failed to delete book")
        }

        books = bookService.getAll()
        println("All books")

        for (book in books) {
            println(" Book {" +
                    " ID: " + book.id.toString() +
                    " AUTHOR: " +  book.author +
                    " TITLE: " + book.title +
                    " DESCRIPTION: " + book.description  +
                    " PUBLISHED: " + book.published.toString()  +
                    " SCORE: " + book.score.toString() + "}")
        }

        println("------------")
        println("Lab 3 TEST")
        println("------------")

        var test3ID  = -1
        var test3Author  = ""
        var test3Title  = "PogPogovich"
        var test3Description = "uyecghdiuehduudhduvhwldvq"
        var test3Published = 1987
        var test3Score  = 9


        try {
            val addedId = bookService.addBook(test3Author, test3Title, test3Description, test3Published, test3Score)
            println("ADDED ID: $addedId")
        } catch (fault: IllegalBookFieldException) {
            println("SOAP Fault: ${fault.getFaultInfo().message}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }


//        try {
//            val deletedID = bookService.deleteBook(test3ID)
//            println("Deleted ID: $deletedID")
//        } catch (fault: IllegalBookFieldException) {
//            println("SOAP Fault: ${fault.getFaultInfo().message}")
//        }catch (e: Exception) {
//            println("Error: ${e.message}")
//        }



    }
}

