/*
 * Copyright (C) 2016  Irotsoma, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
/*
 * Created by irotsoma on 6/19/2016.
 */
package com.irotsoma.cloudbackenc.centralcontroller

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.system.ApplicationPidFileWriter
//import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
//@EnableEurekaServer
open class CentralController

fun main(args: Array<String>) {
    val centralController = SpringApplication(CentralController::class.java, *args)
    centralController.addListeners(ApplicationPidFileWriter("eureka-server.pid"))
    centralController.run()

}