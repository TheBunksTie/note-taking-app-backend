package com.thermondo.domain.common

/**
 * Base class for all domain related exception messages
 */
abstract class DomainException(message: String) : RuntimeException(message) {
}
