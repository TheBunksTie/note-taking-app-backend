package com.thermondo.usecase.common

import com.thermondo.usecase.abstraction.IOperationResultViewModel

/**
 * Abstract base class for use cases defining a [RequestModel] and a [ResultModel]
 * @param TRequestModel the general request model, which the concrete use case must implement in detail
 * @param TResultModel the general result model, which the concrete use case must implement in detail
 */
abstract class UseCase<TRequestModel : UseCase.RequestModel, TResultModel : UseCase.ResultModel> {

    abstract fun execute(input: TRequestModel) : TResultModel

    interface RequestModel

    interface ResultModel : IOperationResultViewModel
}
