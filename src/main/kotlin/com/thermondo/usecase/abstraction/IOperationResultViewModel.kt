package com.thermondo.usecase.abstraction

/**
 * Marker interface for all operation result view models
 * @property successful status flag indication the success of the operation
 * @property message status message containing additional operation result details
 */
interface IOperationResultViewModel : IViewModel {
    val successful: Boolean
    val message: String
}
