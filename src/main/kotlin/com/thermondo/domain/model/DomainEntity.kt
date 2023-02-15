package com.thermondo.domain.model

import com.thermondo.domain.abstraction.IDomainEntity

/**
 * Abstract base class for domain entity containing the most important/denoting properties
 * @property id unique id of the domain entity
 * @property createdAt timestamp when domain entity was originally created
 * @property changedAt timestamp when domain entity was modified last
 */
abstract class DomainEntity(var id: Id, var createdAt: CreatedAt, var changedAt: ChangedAt) : IDomainEntity {

    override fun id() : Id {
        return id
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName} [id='$id']"
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other == null)
            return false
        if (this.javaClass != other.javaClass)
            return false

        if (other !is DomainEntity) return false

        return id == other.id
    }
}
