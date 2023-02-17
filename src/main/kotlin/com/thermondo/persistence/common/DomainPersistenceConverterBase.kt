package com.thermondo.persistence.common

import com.thermondo.domain.abstraction.IDomainEntity
import com.thermondo.persistence.abstraction.IDomainPersistenceConverter
import com.thermondo.persistence.abstraction.IPersistenceEntity
import com.thermondo.usecase.common.ObjectConverterBase

/**
 * Base class for object conversion between [IDomainEntity] and [IPersistenceEntity]
 * @param T1 conversion type operand 1 derived from [IDomainEntity]
 * @param T2 conversion type operand 2 derived from [IPersistenceEntity]
 * @nested PersistenceConversionException exception indicating an exception while converting
 * between [IDomainEntity] and [IPersistenceEntity]
 */
abstract class DomainPersistenceConverterBase<T1 : IDomainEntity, T2 : IPersistenceEntity> :
    ObjectConverterBase<T1, T2>(), IDomainPersistenceConverter<T1, T2> {

    /**
     * Specific [PersistenceException] indicating an exception while converting
     * between [IDomainEntity] and [IPersistenceEntity]
     */
    class PersistenceConversionException(e: Exception) : PersistenceException(e.message.toString(), e)
}
