package com.thermondo.persistence.abstraction

import com.thermondo.domain.abstraction.IDomainEntity
import com.thermondo.usecase.abstraction.IObjectConverter

/**
 * Marker interface for object conversion between [IDomainEntity] and [IPersistenceEntity]
 * @param T1 conversion type operand 1 derived from [IDomainEntity]
 * @param T2 conversion type operand 2 derived from [IPersistenceEntity]
 */
interface IDomainPersistenceConverter<T1 : IDomainEntity, T2 : IPersistenceEntity> : IObjectConverter<T1, T2>
