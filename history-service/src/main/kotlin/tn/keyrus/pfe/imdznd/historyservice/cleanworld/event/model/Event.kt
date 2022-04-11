package tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model

import io.vavr.control.Either
import java.time.LocalDateTime
import java.util.*

class Event private constructor(
    val action: EventAction,
    val objectId: String,
    val eventTime: LocalDateTime
) {
    override fun toString() =
        """Event(
        |   action='$action',
        |   objectId='$objectId',         
        |   creationTime='$eventTime',
        |)"""
            .trimMargin()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Event
        if (action != other.action) return false
        if (objectId != other.objectId) return false
        if (eventTime != other.eventTime) return false
        return true
    }

    override fun hashCode(): Int {
        var result = action.hashCode()
        result = 31 * result + objectId.hashCode()
        result = 31 * result + eventTime.hashCode()
        return result
    }

    companion object Builder {

        fun of(
            action: EventAction,
            objectId: String,
            eventTime: LocalDateTime,
        ) =
            checkEvent(
                objectId,
                eventTime
            )
                .let {
                    checkErrors(
                        it,
                        action,
                        objectId,
                        eventTime
                    )
                }

        private fun checkErrors(
            eventErrors: Sequence<EventError> = emptySequence(),
            action: EventAction,
            objectId: String,
            eventTime: LocalDateTime
        ): Either<Sequence<EventError>, Event> =
            if (eventErrors.count() == 0)
                Either.right(
                    Event(
                        action,
                        objectId,
                        eventTime
                    )
                )
            else
                Either.left(eventErrors)

        private fun checkEvent(
            objectId: String,
            eventTime: LocalDateTime
        ): Sequence<EventError> =
            sequenceOf(
                checkObjectId(objectId),
                checkEventTime(eventTime),
            )
                .filter { it.isPresent }
                .map { it.get() }

        private fun checkEventTime(eventTime: LocalDateTime): Optional<EventError> =
            checkField(
                eventTime,
                EventError.EventTimeError
            ) { it.isAfter(LocalDateTime.now()).not() }

        private fun checkObjectId(objectId: String) =
            checkField(
                objectId,
                EventError.EventObjectIdError,
                String::isNotEmpty
            )

        private fun <T> checkField(
            field: T,
            error: EventError,
            validCondition: (T) -> Boolean
        ) =
            if (validCondition(field))
                Optional.empty()
            else
                Optional.of(error)

    }

    enum class EventAction {
        SAVEUSER,
        UPDATEUSER,
        DELETEUSER,
        FLAGUSER,
        FLAGTRANSACTION
    }

    sealed interface EventError {
        object EventObjectIdError : EventError
        object EventTimeError : EventError
    }

}