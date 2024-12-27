package org.example


import org.apache.juddi.v3.client.config.UDDIClient
import org.uddi.api_v3.*
import java.io.File


class JuddiClient {
    private val uddiClient = UDDIClient("META-INF/uddi.xml")
    private val clerk = uddiClient.getClerk("default")
    private val transport = uddiClient.getTransport("default")

    fun checkAvailability()  {
        try  {
            if (clerk == null)
                throw Exception("the clerk wasn't found, check the config file!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun findBusinessByName(businessName: String): BusinessInfo? {
        try {
            if (clerk == null) {
                throw Exception("Clerk is not initialized. Check the configuration.")
            }

            val findBusiness = FindBusiness()
            val name = Name()
            name.value = businessName
            findBusiness.name.add(name)

            val businessList = transport.uddiInquiryService.findBusiness(findBusiness).businessInfos

            if (businessList == null || businessList.businessInfo.isEmpty()) {
                println("No business found with name: $businessName")
                return null
            }

            return businessList.businessInfo[0]

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun findServiceByName(serviceName: String): ServiceInfo? {
        try {
            if (clerk == null) {
                throw Exception("Clerk is not initialized. Check the configuration.")
            }

            val findService = FindService()
            val name = Name()
            name.value = serviceName
            findService.name.add(name)

            val servList = transport.uddiInquiryService.findService(findService).serviceInfos

            if (servList ==  null || servList.serviceInfo.isEmpty()) {
                println("No service found with name: $serviceName")
                return null
            }

            return servList.serviceInfo[0]

        } catch(e: Exception)  {
            e.printStackTrace()
            return  null
        }
    }

    fun publishBusiness(bName: String) {
        try {
            val myBusEntity = BusinessEntity()

            if  (myBusEntity == null) {
                throw  IllegalArgumentException("Did not find business with identifier ${bName}")
            }

            val myBusName = Name()
            myBusName.value = bName
            myBusEntity.name.add(myBusName)

            val register: BusinessEntity = clerk.register(myBusEntity)


            println("Busienss ${bName} is regisited with key:  ${register.businessKey}!")

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun publishService(sName: String, bName: String, sWSDL: String) {
        try {
            val foundBusiness = findBusinessByName(bName)

            val myService = BusinessService()
            myService.businessKey = foundBusiness?.businessKey
            val mySerName = Name()
            mySerName.value = sName
            myService.name.add(mySerName)

            val bindingTemplate = BindingTemplate()
            bindingTemplate.accessPoint = AccessPoint(sWSDL, "endPoint")
            bindingTemplate.accessPoint.useType = "wsdlDeployment"

            val bindingTemplates = BindingTemplates()
            bindingTemplates.bindingTemplate.add(bindingTemplate)
            myService.bindingTemplates = bindingTemplates


            val registeredService: BusinessService? = clerk.register(myService)
            if (registeredService == null) {
                println("Service registration failed!")
                return
            }

            println("BusinessServiece ${sName} is registered successfully with key: ${registeredService.serviceKey}")

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }


    }

    fun getWSDLLink(serviceKey: String): String? {
        try  {
            val servDetails = clerk.getServiceDetail(serviceKey)
            val bt = servDetails.bindingTemplates

            val res  = bt.bindingTemplate.firstOrNull()
            if (res == null) {
                println("Service does not have Binding Template values")
                return  ""
            }

            return  res.accessPoint.value

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun close() {
        clerk.discardAuthToken()
    }
}




suspend fun main()  {
    val newClient = JuddiClient()
    newClient.checkAvailability()

    var action = ""
    while (true) {
        println("Actions:\n1) register\n2) find\n3) exit")
        action = readln()

        when (action) {
            "exit"  -> {
                newClient.close()
                println("Connection is closed")
                break
            }

            "register" -> {
                var opt = ""
                while (true) {
                    println("Type \"1\" if want to register business")
                    println("Type \"2\" if want to register service")
                    opt = readln()

                    if (opt != "1" && opt != "2") {
                        continue

                    }else {

                        when (opt){
                            "1"  -> {
                                println("Fill business's name:")
                                val bName = readln()
                                newClient.publishBusiness(bName)
                            }
                            "2" -> {
                                println("Fill service's name:")
                                val sName = readln()
                                println("Fill business's name:")
                                val bName = readln()
                                println("Fill WSDL's link:")
                                val wsdlLink = readln()

                                newClient.publishService(sName, bName, wsdlLink)
                            }
                        }

                        break
                    }
                }
            }
            "find" -> {
                var opt = ""
                while (true) {
                    println("Type \"1\" if want to find business")
                    println("Type \"2\" if want to find service")
                    opt = readln()

                    if (opt != "1" && opt != "2") {
                        continue

                    } else {

                        when (opt) {
                            "1"  -> {
                                println("Fill business's name:")
                                val bName = readln()
                                val res = newClient.findBusinessByName(bName)
                                println("Business name: ${res?.name}")
                                println("Business key: ${res?.businessKey}")
                            }
                            "2" -> {
                                println("Fill service's name:")
                                val sName = readln()
                                val res = newClient.findServiceByName(sName)
                                println("Service name: ${res?.name}")
                                println("Business key: ${res?.businessKey}")
                                println("Service key: ${res?.serviceKey}")
                                println("Service end point: ${newClient.getWSDLLink(res?.serviceKey.toString())}")

                                var choice  = ""
                                while (true) {
                                    println("Do you want to connect to the ${sName} service ?")
                                    println("Type \"y\" - Yes or \"n\" - No")
                                    choice = readln()

                                    if (choice != "y" && choice != "n") {
                                        continue

                                    }else {

                                        when (choice) {
                                            "n" -> break
                                            "y"  ->  {
                                                println("Connection...")
                                                val myClient = WebClient()
                                                myClient.run(newClient.getWSDLLink(res?.serviceKey.toString()).toString())

                                            }
                                        }
                                    }
                                }
                            }
                        }

                        break
                    }

                }
            }
        }



    }


}