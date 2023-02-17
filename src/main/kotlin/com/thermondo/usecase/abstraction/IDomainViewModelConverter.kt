package com.thermondo.usecase.abstraction

import com.thermondo.domain.abstraction.IDomainEntity

/**
 * Marker interface for object conversion between [IDomainEntity] and [IDomainViewModel]
 * @param TDomainEntity conversion type operand 1 derived from [IDomainEntity]
 * @param TDomainViewModel conversion type operand 2 derived from [IDomainViewModel]
 */
interface IDomainViewModelConverter<TDomainEntity : IDomainEntity, TDomainViewModel : IDomainViewModel>
    : IObjectConverter<TDomainEntity, TDomainViewModel>
