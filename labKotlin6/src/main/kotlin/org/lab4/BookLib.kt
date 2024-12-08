package org.lab4

import kotlinx.serialization.Serializable

@Serializable
class BookLib {
    var id: Int = 0
    var author: String? = null
    var title: String? = null
    var description: String? = null
    var published: Int = 0
    var score: Int = 0


    constructor()

    constructor(id:  Int,  author: String, title: String, description: String, published: Int, score: Int) {
        this.id = id
        this.author = author
        this.title = title
        this.description = description
        this.published = published
        this.score = score
    }

    override fun toString():  String {
        return ("BookService {" + "ID=" + id +  "AUTHOR=" +  author +  "TITLE=" + title + "DESCRIPTION=" + description  + "PUBLISHED=" + published  + "SCORE=" + score + "}")
    }
}

/*
package org.lab4

import kotlinx.serialization.Serializable

@Serializable
class BookLib {
    var id: Int = 0
    var author: String? = null
    var title: String? = null
    var description: String? = null
    var published: Int = 0
    var score: Int = 0


    constructor()

    constructor(id:  Int,  author: String, title: String, description: String, published: Int, score: Int) {
        this.id = id
        this.author = author
        this.title = title
        this.description = description
        this.published = published
        this.score = score
    }

    override fun toString():  String {
        return ("BookService {" + "ID=" + id +  "AUTHOR=" +  author +  "TITLE=" + title + "DESCRIPTION=" + description  + "PUBLISHED=" + published  + "SCORE=" + score + "}")
    }
}


 */