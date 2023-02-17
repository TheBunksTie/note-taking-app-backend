package com.thermondo.persistence.abstraction

import com.thermondo.domain.abstraction.IDomainEntity
import com.thermondo.usecase.abstraction.IObjectConverter

/**
 * Marker interface for object conversion between [IDomainEntity] and [IPersistenceEntity]
 * @param TDomainEntity conversion type operand 1 derived from [IDomainEntity]
 * @param TPersistenceEntity conversion type operand 2 derived from [IPersistenceEntity]
 */
interface IDomainPersistenceConverter<TDomainEntity : IDomainEntity, TPersistenceEntity
    : IPersistenceEntity> : IObjectConverter<TDomainEntity, TPersistenceEntity>
