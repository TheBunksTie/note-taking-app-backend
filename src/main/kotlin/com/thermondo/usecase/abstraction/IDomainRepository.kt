package com.thermondo.usecase.abstraction

import com.thermondo.domain.abstraction.IDomainEntity

/**
 * Marker interface for [IDomainEntity]-storing repository
 * @param T actual stored type of repository, derived from [IDomainEntity]
 */
interface IDomainRepository<T: IDomainEntity> : IRepository<T> {
}
