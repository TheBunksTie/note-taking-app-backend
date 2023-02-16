package com.thermondo.persistence.common

import com.thermondo.domain.model.DomainEntity
import com.thermondo.persistence.abstraction.IPersistenceEntity

/**
 * Abstract base class for persistence entities with main properties [id], [createdAt], [changedAt]
 * mirroring the [DomainEntity] for persistence layer
 * @property id unique id of the persistence entity
 * @property createdAt timestamp when persistence entity was originally created
 * @property changedAt timestamp when persistence entity was modified last
 */
abstract class PersistenceEntityBase(val id: String, val createdAt: String, val changedAt: String) :
    IPersistenceEntity {
}
