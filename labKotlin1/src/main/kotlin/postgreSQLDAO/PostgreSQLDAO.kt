package org.example.postgreSQLDAO

import org.example.connection.ConnectionUtil
import org.example.entity.book.Book

import java.sql.SQLException
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger


class PostgreSQLDAO {

    val all: List<Book>
        get() {
            val books: MutableList<Book> = ArrayList()
            try {
                ConnectionUtil.connection.use { connection ->
                    val stmt: Statement? = connection?.createStatement()
                    val rs = stmt?.executeQuery("select * from books") ?: return emptyList()
                    while (rs.next()) {
                        val id = rs.getInt("id")
                        val author = rs.getString("author")
                        val title = rs.getString("title")
                        val description = rs.getString("description")
                        val published = rs.getInt("published")
                        val score = rs.getInt("score")

                        val book = Book(id, author, title, description, published, score)
                        books.add(book)
                    }
                }
            } catch (ex: SQLException) {
                Logger.getLogger(PostgreSQLDAO::class.java.name).log(
                    Level.SEVERE,
                    null, ex
                )
            }
            return books
        }



    fun getBookByValues(
        author: String? = null,
        title: String? = null,
        description: String? = null,
        published: Int = 0,
        score: Int =  0
    ): List<Book> {
        val books: MutableList<Book> = ArrayList()
        val query = StringBuilder("SELECT * FROM books WHERE 1=1")


        if (!author.isNullOrEmpty()) {
            query.append(" AND author = ?")
        }
        if (!title.isNullOrEmpty()) {
            query.append(" AND title = ?")
        }
        if (!description.isNullOrEmpty()) {
            query.append(" AND description LIKE ?")
        }
        if (published > 0) {
            query.append(" AND published = ?")
        }
        if (score > 0) {
            query.append(" AND score = ?")
        }

        try {
            ConnectionUtil.connection.use { connection ->
                val stmt = connection?.prepareStatement(query.toString())
                var index = 1

                if (!author.isNullOrEmpty()) {
                    stmt?.setString(index++, author)
                }
                if (!title.isNullOrEmpty()) {
                    stmt?.setString(index++, title)
                }
                if (!description.isNullOrEmpty()) {
                    stmt?.setString(index++, "%$description%")
                }
                if (published > 0) {
                    stmt?.setInt(index++, published)
                }
                if (score > 0) {
                    stmt?.setInt(index++, score)
                }

                val rs = stmt?.executeQuery()
                while (rs?.next() == true) {
                    val id = rs.getInt("id")
                    val bookAuthor = rs.getString("author")
                    val bookTitle = rs.getString("title")
                    val bookDescription = rs.getString("description")
                    val bookPublished = rs.getInt("published")
                    val bookScore = rs.getInt("score")

                    val book = Book(id, bookAuthor, bookTitle, bookDescription, bookPublished, bookScore)
                    books.add(book)

                }
            }

        } catch (ex: SQLException) {
            Logger.getLogger(PostgreSQLDAO::class.java.name).log(Level.SEVERE, null, ex)
        }

        return  books
    }


}