package com.thermondo.domain.model

import com.thermondo.domain.common.DomainException

/**
 * DomainException denoting an invalid argument was passed into the creation of an [Id]
 */
class InvalidIdException(invalidIdValue: String) : DomainException("Invalid id value '$invalidIdValue'") {

}
