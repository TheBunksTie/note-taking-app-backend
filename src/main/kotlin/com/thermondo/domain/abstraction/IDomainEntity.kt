package com.thermondo.domain.abstraction

import com.thermondo.domain.model.Id

/**
 * Marking interface for domain entity
 */
interface IDomainEntity : IDomainObject {
    /**
     * returns the id of a domain entity
     */
    fun id() : Id
}
