package moe.nerinyan.apiv3.service

import java.lang.Exception

interface NotificationService {
    fun startup()
    fun shutdown()

    fun info(message: String)
    fun error(message: String)
    fun error(exception: Exception)

}