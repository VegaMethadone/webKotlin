package org.example


import java.net.HttpURLConnection
import java.net.URL



class WebClient {

    fun callGetAll(newUrl: String) {
        val url = URL(newUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8")
        connection.setRequestProperty("SOAPAction", "http://service.example.org/BookService/getAllRequest")

        connection.doOutput = true

        val xmlRequest = """
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tns="http://service.example.org/">
           <soapenv:Header/>
           <soapenv:Body>
              <tns:getAll/>
           </soapenv:Body>
        </soapenv:Envelope>
    """.trimIndent()

        connection.outputStream.use { output ->
            output.write(xmlRequest.toByteArray())
        }

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            connection.inputStream.use { input ->
                val response = input.bufferedReader().readText()
                prettyPrintXml(response)
            }
        } else {
            println("Error when sending the request: $responseCode")
        }

        connection.disconnect()
    }

    fun prettyPrintXml(xml: String) {
        try {
            val inputSource = org.xml.sax.InputSource(xml.reader())
            val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(inputSource)
            val transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes")
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8")
            val result = java.io.StringWriter()
            transformer.transform(javax.xml.transform.dom.DOMSource(doc), javax.xml.transform.stream.StreamResult(result))
            println(result.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun run(urlWsdl: String)  {
        println("Connection is established")
        val newUrl = if (urlWsdl.endsWith("?wsdl")) {
            urlWsdl.substring(0, urlWsdl.length - 5)
        }else {
            println("Link is not correct")
            return
        }

        var opt =  ""
        while (true) {
            println("Type \"1\" to getAll")
            println("Type \"e\" to close connection")
            opt =  readln()

            when (opt) {
                "e"  -> {
                    println("Connection is closed...")
                    return
                }
                "1" -> {
                    callGetAll(newUrl)
                }
            }

        }

    }
}