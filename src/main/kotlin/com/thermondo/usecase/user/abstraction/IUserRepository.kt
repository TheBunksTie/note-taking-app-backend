package com.thermondo.usecase.user.abstraction

import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.usecase.abstraction.IDomainRepository

/**
 * Extension interface to default repository to add more actions for accessing
 * the [User] data
 */
interface IUserRepository : IDomainRepository<User> {
    fun exists(userName: UserName): Boolean
    fun getBy(userName: UserName): User?
}
