package moe.nerinyan.apiv3.config


import moe.nerinyan.apiv3.service.NotificationService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.DisposableBean

@Configuration
class BootRunner(
    private val notificationService: NotificationService
) : ApplicationRunner, DisposableBean {

    override fun run(args: ApplicationArguments?) {
        notificationService.startup()
    }

    override fun destroy() {
        notificationService.shutdown()
    }
}
