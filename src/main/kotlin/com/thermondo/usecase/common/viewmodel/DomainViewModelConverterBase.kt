package com.thermondo.usecase.common.viewmodel

import com.thermondo.domain.abstraction.IDomainEntity
import com.thermondo.usecase.abstraction.IDomainViewModel
import com.thermondo.usecase.abstraction.IDomainViewModelConverter
import com.thermondo.usecase.common.ObjectConverterBase
import com.thermondo.usecase.common.UseCase

/**
 * Base class for object conversion between [IDomainEntity] and [IDomainViewModel]
 * @param T1 conversion type operand 1 derived from [IDomainEntity]
 * @param T2 conversion type operand 2 derived from [IDomainViewModel]
 * @nested UseCaseConversionException exception indicating an exception while converting
 * between [IDomainEntity] and [IDomainViewModel]
 */
abstract class DomainViewModelConverterBase<T1 : IDomainEntity, T2 : IDomainViewModel> :
    ObjectConverterBase<T1, T2>(), IDomainViewModelConverter<T1, T2> {

    /**
     * Specific [UseCase.UseCaseException] indicating an exception while converting
     * between [IDomainEntity] and [IDomainViewModel]
     */
    class UseCaseConversionException(e: Exception) : UseCase.UseCaseException(e.message.toString(), e)
}
