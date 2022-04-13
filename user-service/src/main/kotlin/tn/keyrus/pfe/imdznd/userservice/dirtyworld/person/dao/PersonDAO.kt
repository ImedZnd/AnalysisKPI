package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao

import org.springframework.data.relational.core.mapping.Table
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

@Table("person")
class PersonDAO(
    val seqUser: Int,
    val failedSignInAttempts: Int,
    val birthYear: Year,
    val state: Person.PersonState,
    val country: Country,
    val createdDate: LocalDateTime,
    val termsVersion: LocalDate,
    val phoneCountry: String,
    val kyc: Person.PersonKYC,
    val hasEmail: Boolean,
    val numberOfFlags: Int,
    val isFraudster: Boolean,
    ) {

}