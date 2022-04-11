package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@ComponentScan("tn.keyrus.pfe.imdznd.historyservice.**")
@EnableR2dbcRepositories("tn.keyrus.pfe.imdznd.historyservice.**")
@ConfigurationPropertiesScan(basePackages = ["tn.keyrus.pfe.imdznd.historyservice.**"])
class HistoryServiceApplication

fun main(args: Array<String>) {
    runApplication<HistoryServiceApplication>(*args)
}
