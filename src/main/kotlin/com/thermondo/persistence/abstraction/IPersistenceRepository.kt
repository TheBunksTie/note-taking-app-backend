package com.thermondo.persistence.abstraction

import com.thermondo.usecase.abstraction.IRepository

/**
 * Marker interface for persistence entity repositories of type [T] derived from [IPersistenceEntity]
 * @param T repository type derived from [IPersistenceEntity]
 */
interface IPersistenceRepository<T : IPersistenceEntity> : IRepository<T>
