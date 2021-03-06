package tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model

import io.vavr.control.Either
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.Optional

class Person private constructor(
    val seqUser: Int,
    val failedSignInAttempts: Int,
    val birthYear: Year,
    val state: PersonState,
    val country: Country,
    val createdDate: LocalDateTime,
    val termsVersion: LocalDate,
    val phoneCountry: String,
    val kyc: PersonKYC,
    val hasEmail: Boolean,
    val numberOfFlags: Int,
    val isFraudster: Boolean,
    ) {

    companion object Builder {
        fun of(
            seqUser: Int,
            failedSignInAttempts: Int,
            birthYear: Year,
            country: Country,
            createdDate: LocalDateTime,
            termsVersion: LocalDate,
            phoneCountry: String,
            kyc: PersonKYC,
            state: PersonState,
            hasEmail: Boolean,
            numberOfFlags: Int,
            isFraudster: Boolean,
        ) =
            checkPerson(
                seqUser,
                failedSignInAttempts,
                birthYear,
                country,
                createdDate,
                termsVersion,
                phoneCountry,
                numberOfFlags
            )
                .let {
                    checkPersonErrors(
                        it,
                        seqUser,
                        failedSignInAttempts,
                        birthYear,
                        state,
                        country,
                        createdDate,
                        termsVersion,
                        phoneCountry,
                        kyc,
                        hasEmail,
                        numberOfFlags,
                        isFraudster,
                    )
                }

        private fun checkPersonErrors(
            personErrors: Sequence<PersonError>,
            seqUser: Int,
            failedSignInAttempts: Int,
            birthYear: Year,
            state: PersonState,
            country: Country,
            createdDate: LocalDateTime,
            termsVersion: LocalDate,
            phoneCountry: String,
            kyc: PersonKYC,
            hasEmail: Boolean,
            numberOfFlags: Int,
            isFraudster: Boolean,
        ): Either<Sequence<PersonError>, Person> =
            if (personErrors.count() == 0)
                Either.right(
                    Person(
                        seqUser,
                        failedSignInAttempts,
                        birthYear,
                        state,
                        country,
                        createdDate,
                        termsVersion,
                        phoneCountry,
                        kyc,
                        hasEmail,
                        numberOfFlags,
                        isFraudster,
                    )
                )
            else
                Either.left(personErrors)

        private fun checkPerson(
            seqUser: Int,
            failedSignInAttempts: Int,
            birthYear: Year,
            country: Country,
            createdDate: LocalDateTime,
            termsVersion: LocalDate,
            phoneCountry: String,
            numberOfFlags: Int,
        ): Sequence<PersonError> =
            sequenceOf(
                checkSeqUser(seqUser),
                checkFailedSignInAttempts(failedSignInAttempts),
                checkBirthYear(birthYear),
                checkCountry(country),
                checkCreatedDate(createdDate),
                checkTermsVersion(termsVersion),
                checkPhoneCountry(phoneCountry),
                checkNumberOfFlags(numberOfFlags)
            )
                .filter { it.isPresent }
                .map { it.get() }

        private fun checkNumberOfFlags(numberOfFlags: Int) =
            checkIntegerFields(numberOfFlags, PersonError.PersonNumberOfFlagsError)

        private fun checkPhoneCountry(phoneCountry: String) =
            checkField(
                phoneCountry,
                PersonError.PersonPhoneCountryError,
                String::isNotEmpty
            )

        private fun checkTermsVersion(termsVersion: LocalDate) =
            checkField(
                termsVersion,
                PersonError.PersonTermsVersionError,
            ) { termsVersion.isAfter(LocalDate.now()).not() }

        private fun checkCreatedDate(createdDate: LocalDateTime) =
            checkField(
                createdDate,
                PersonError.PersonCreatedDateError,
            ) { createdDate.isAfter(LocalDateTime.now()).not() }

        private fun checkCountry(country: Country) =
            checkField(
                country,
                PersonError.PersonCountryError,
            ) {
                Country.of(
                    country.code,
                    country.name,
                    country.code3,
                    country.numCode,
                    country.phoneCode
                ).isRight
            }

        private fun checkBirthYear(birthYear: Year) =
            checkField(
                birthYear,
                PersonError.PersonBirthYearError,
            ) { birthYear.isAfter(Year.now()).not() }

        private fun checkSeqUser(seqUser: Int) =
            checkIntegerFields(seqUser, PersonError.PersonSeqUserError)

        private fun checkFailedSignInAttempts(failedSignInAttempts: Int) =
            checkIntegerFields(failedSignInAttempts , PersonError.PersonFailedSignInAttemptsError)

        private fun checkIntegerFields(int: Int ,personError:PersonError ) =
            checkField(
                int,
                personError,
            ) { int >= 0 }

        private fun <T> checkField(
            field: T,
            error: PersonError,
            validCondition: (T) -> Boolean
        ) =
            if (validCondition(field))
                Optional.empty()
            else
                Optional.of(error)

    }

    enum class PersonState {
        ACTIVE,
        INACTIVE
    }

    enum class PersonKYC {
        PASSED,
        FAILED,
        NONE,
    }

    sealed interface PersonError {
        object PersonSeqUserError : PersonError
        object PersonFailedSignInAttemptsError : PersonError
        object PersonBirthYearError : PersonError
        object PersonCountryError : PersonError
        object PersonCreatedDateError : PersonError
        object PersonTermsVersionError : PersonError
        object PersonPhoneCountryError : PersonError
        object PersonNumberOfFlagsError : PersonError
    }
}