package com.irotsoma.cloudbackenc.centralcontroller.cloudservices

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.readValue
import com.irotsoma.cloudbackenc.cloudservice.*
import com.irotsoma.cloudbackenc.common.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarFile
import javax.annotation.PostConstruct

/**
 * Created by irotsoma on 6/20/2016.
 *
 * Imports and stores information about installed Cloud Service Extensions
 */
@Component
open class CloudServiceRepository : ApplicationContextAware {
    companion object { val LOG by logger() }
    //inject settings
    @Autowired lateinit var cloudServicesSettings: CloudServicesSettings
    var cloudServiceExtensions  = emptyMap<UUID,Class<CloudServiceFactory>>()
    var cloudServiceNames = CloudServiceExtensionList()
    //application context must be set before
    lateinit var _applicationContext : ConfigurableApplicationContext
    override fun setApplicationContext(applicationContext: ApplicationContext?) {
        _applicationContext = applicationContext as ConfigurableApplicationContext? ?: throw CloudServiceException("Application context in CloudServiceRepository is null.")
    }

    @PostConstruct
    fun loadDynamicServices() {
        val extensionsDirectory: File = File(cloudServicesSettings.directory)
        if (!extensionsDirectory.isDirectory || !extensionsDirectory.canRead()) {
            LOG.warn("Extensions directory is missing or unreadable. ${extensionsDirectory.absolutePath}")
            return
        }
        var jarURLs = emptyArray<URL>()
        var factoryClasses = emptyMap<UUID,String>()

        for (jar in extensionsDirectory.listFiles{directory, name -> (!File(directory,name).isDirectory && name.endsWith(".jar"))} ?: arrayOf<File>()) {
            try {
                val jarFile = JarFile(jar)
                //read config file from jar if present
                val jarFileEntry = jarFile.getEntry(cloudServicesSettings.configFileName)
                if (jarFileEntry == null) {
                    LOG.debug("Extension missing config file named ${cloudServicesSettings.configFileName}. Skipping jar file: ${jar.absolutePath}")
                }
                else {
                    //get Json config file data
                    val jsonValue = jarFile.getInputStream(jarFileEntry).reader().readText()
                    val mapper = ObjectMapper().registerModule(KotlinModule())
                    val mapperData: CloudServiceExtensionConfig = mapper.readValue(jsonValue)
                    //add values to maps for consumption later
                    val cloudServiceUUID = UUID.fromString(mapperData.serviceUUID)
                    factoryClasses = factoryClasses.plus(Pair(cloudServiceUUID,mapperData.packageName+"."+mapperData.factoryClass))
                    cloudServiceNames.add(CloudServiceExtension(cloudServiceUUID,mapperData.serviceName))
                    jarURLs = jarURLs.plus(jar.toURI().toURL())
                }
            } catch (e: MissingKotlinParameterException) {
                LOG.warn("Cloud service extension configuration file is missing a required field.  This extension will be unavailable: ${jar.name}.  Error Message: ${e.message}")
            } catch (e: Exception) {
                LOG.warn("Error processing cloud service extension file. This extension will be unavailable: ${jar.name}.   Error Message: ${e.message}")
            }
        }
        //create a class loader with all of the jars
        val classLoader = URLClassLoader(jarURLs,_applicationContext.classLoader)
        //cycle through all of the classes, make sure they inheritors CloudServiceFactory, and add them to the list
        for ((key, value) in factoryClasses) {
            val gdClass = classLoader.loadClass(value)
            //verify instance of gdClass is a CloudServiceFactory
            if (gdClass.newInstance() is CloudServiceFactory) {
                //add to list -- suppress warning about unchecked class as we did that in the if statement for an instance but it can't be done directly
                cloudServiceExtensions = cloudServiceExtensions.plus(Pair(key, @Suppress("UNCHECKED_CAST")(gdClass as Class<CloudServiceFactory>)))
            }
            else {
                LOG.warn("Error loading cloud service extension: Factory is not an instance of CloudServiceFactory: $value" )
            }
        }
    }
}
