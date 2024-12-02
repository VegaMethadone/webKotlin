package org.lab4

import java.sql.*


class BookDAO {
    private var jdbcConnection: Connection? = null

    @Throws(SQLException::class)
    protected fun connect() {
        if (jdbcConnection == null || jdbcConnection!!.isClosed) {
            jdbcConnection = ConnectionUtil.connection;
        }
    }

    @Throws(SQLException::class)
    protected fun disconnect() {
        if (jdbcConnection != null && !jdbcConnection!!.isClosed) {
            jdbcConnection!!.close()
        }
    }

//    @Throws(SQLException::class)
//    fun insertBook(book: BookLib?): Boolean {
//        val sql = "INSERT INTO books (author, title, description, published, score) VALUES (?, ?, ?, ?, ?)"
//        connect()
//
//        if (book == null) {
//            throw SQLException("book cannot be null")
//        }
//        if (jdbcConnection == null) {
//            throw SQLException("jdb connection is null")
//        }
//
//        val statement = jdbcConnection!!.prepareStatement(sql)
//        statement.setString(1, book.author)
//        statement.setString(2, book.title)
//        statement.setString(3, book.description)
//        statement.setInt(4, book.published)
//        statement.setString(5, book.score.toString())
//
//        val rowInserted = statement.executeUpdate() > 0
//        statement.close()
//        disconnect()
//        return rowInserted
//    }
    @Throws(SQLException::class)
    fun insertBook(book: BookLib?): Boolean {
        val sql = "INSERT INTO books (author, title, description, published, score) VALUES (?, ?, ?, ?, ?)"
        connect()

        if (book == null) {
            throw SQLException("book cannot be null")
        }
        if (jdbcConnection == null) {
            throw SQLException("jdb connection is null")
        }

        val statement = jdbcConnection!!.prepareStatement(sql)
        statement.setString(1, book.author)
        statement.setString(2, book.title)
        statement.setString(3, book.description)
        statement.setInt(4, book.published)
        statement.setInt(5, book.score)

        val rowInserted = statement.executeUpdate() > 0
        statement.close()
        disconnect()
        return rowInserted
    }

    @Throws(SQLException::class)
    fun listAllBooks(): List<BookLib> {
        val listBook: MutableList<BookLib> = ArrayList()

        val sql = "SELECT * FROM books"

        connect()

        val statement = jdbcConnection!!.createStatement()
        val resultSet = statement.executeQuery(sql)

        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val title = resultSet.getString("author")
            val author = resultSet.getString("title")
            val description = resultSet.getString("description")
            val published = resultSet.getInt("published")
            val score = resultSet.getInt("score")

            val book = BookLib(id, author, title, description, published, score)
            listBook.add(book)
        }

        resultSet.close()
        statement.close()

        disconnect()

        return listBook
    }

    @Throws(SQLException::class)
    fun getBook(id: Int): BookLib? {
        var book: BookLib? = null
        val sql = "SELECT * FROM books WHERE id = ?"

        connect()

        val statement = jdbcConnection!!.prepareStatement(sql)
        statement.setInt(1, id)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val title = resultSet.getString("author")
            val author = resultSet.getString("title")
            val description = resultSet.getString("description")
            val published = resultSet.getInt("published")
            val score = resultSet.getInt("score")

            book = BookLib(id, title, author, description, published, score)
        }

        resultSet.close()
        statement.close()

        disconnect()

        return book
    }


    @Throws(SQLException::class)
    fun updateBook(book: BookLib): Boolean {
        val sql = "UPDATE books SET author = ?, title = ?, description = ?, published = ?, score = ? WHERE id = ?"
        connect()

        val statement = jdbcConnection!!.prepareStatement(sql)
        statement.setString(1, book.author)
        statement.setString(2, book.title)
        statement.setString(3, book.description)
        statement.setInt(4, book.published)
        statement.setInt(5, book.score)
        statement.setInt(6, book.id)

        val rowUpdated = statement.executeUpdate() > 0
        statement.close()
        disconnect()
        return rowUpdated
    }

    @Throws(SQLException::class)
    fun deleteBook(id: Int): Boolean {
        val sql = "DELETE FROM books WHERE id = ?"
        connect()

        val statement = jdbcConnection!!.prepareStatement(sql)
        statement.setInt(1, id)

        val rowDeleted = statement.executeUpdate() > 0
        statement.close()
        disconnect()
        return rowDeleted
    }
}

