package com.thermondo.persistence.common

import com.thermondo.domain.abstraction.IDomainEntity
import com.thermondo.domain.model.Id
import com.thermondo.persistence.abstraction.IDomainPersistenceConverter
import com.thermondo.persistence.abstraction.IPersistenceEntity
import com.thermondo.persistence.abstraction.IPersistenceRepository
import com.thermondo.usecase.abstraction.IRepository

/**
 * Abstract base class for repositories connecting [IDomainEntity] and [IPersistenceEntity]
 * @param TDomainEntity type of the [IDomainEntity] to store
 * @param TPersistenceEntity type of the [IPersistenceEntity] to represent the persisted form of an [IDomainEntity]
 * @property converter a two-way converter between [TDomainEntity] and [TPersistenceEntity]
 * @property persistenceRepository actual repository to store [TPersistenceEntity] in
 */
abstract class RepositoryBase<TDomainEntity : IDomainEntity, TPersistenceEntity : IPersistenceEntity>(
    protected val converter: IDomainPersistenceConverter<TDomainEntity, TPersistenceEntity>,
    protected val persistenceRepository: IPersistenceRepository<TPersistenceEntity>
) :
    IRepository<TDomainEntity> {

    override fun persist(persistableObject: TDomainEntity): TDomainEntity {
        val persistenceObject = converter.createFromT1(persistableObject)
        val persistedObject = persistenceRepository.persist(persistenceObject)
        return converter.createFromT2(persistedObject)
    }

    override fun delete(id: Id): Boolean {
        return persistenceRepository.delete(id)
    }

    override fun exists(id: Id): Boolean {
        return persistenceRepository.exists(id)
    }

    override fun getBy(id: Id): TDomainEntity? {
        val persistenceEntity = persistenceRepository.getBy(id) ?: return null
        return converter.createFromT2(persistenceEntity)
    }

    override fun getAll(): List<TDomainEntity> {
        return persistenceRepository.getAll().stream().map { p -> converter.createFromT2(p) }.toList().toList()
    }
}
