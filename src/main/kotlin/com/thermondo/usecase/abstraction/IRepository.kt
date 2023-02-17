package com.thermondo.usecase.abstraction

import com.thermondo.domain.model.Id

/**
 * Marker interface for object repositories of type [T]
 * @param T repository type
 */
interface IRepository<T> {
    fun persist(persistableObject: T): T
    fun delete(id: Id): Boolean
    fun exists(id: Id): Boolean
    fun getBy(id: Id): T?
    fun getAll(): List<T>
}
