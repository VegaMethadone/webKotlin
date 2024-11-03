package org.example.service



data class BookServiceFault(val message: String) {
    companion object {
        fun invalidFieldInstance(): BookServiceFault {
            return BookServiceFault("Invalid field value provided.")
        }

        fun notFoundInstance(): BookServiceFault {
            return BookServiceFault("Record with specified ID does not exist.")
        }
    }
}