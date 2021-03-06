package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO

interface CountryReactiveRepository : ReactiveCrudRepository<CountryDAO, Long> {
    fun findByCode(code : String): Mono<CountryDAO>
    fun findByNumCode(numCode: Int): Mono<CountryDAO>
    fun findByPhoneCode(phoneCode: Int): Mono<CountryDAO>
}