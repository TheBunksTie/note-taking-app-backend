package com.thermondo.persistence.common

/**
 * Abstract base class for any persistence related exception
 */
abstract class PersistenceException(message: String, innerException: Exception?) : RuntimeException(message, innerException)
