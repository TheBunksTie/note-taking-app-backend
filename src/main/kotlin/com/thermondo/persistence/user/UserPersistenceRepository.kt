package com.thermondo.persistence.user

import com.thermondo.domain.model.Id
import com.thermondo.persistence.user.abstraction.IUserPersistenceRepository
import java.time.LocalDateTime

/**
 * In memory repository to store [UserPersistenceEntity]
 * Not to be used for production use, only for demonstration purposes
 */
class UserPersistenceRepository : IUserPersistenceRepository {

    // TODO obviously in production there would be a mechanism to load test data from a file
    //  or populate a DB via script and no hardcoded values here
    companion object TestConstants {
        const val testUser1Id = "ccfacbb6-51b8-4598-a730-8fc4a7563da6"
        const val testUser1Name = "testUser1"
        const val testUser2Id = "041a33fe-05d1-4d79-9dfd-9e2a065fe3b7"
        const val testUser2Name = "testUser2"
    }

    private val userMap: MutableMap<String, UserPersistenceEntity> = mutableMapOf(
        testUser1Id to initTestUser(testUser1Id, testUser1Name),
        testUser2Id to initTestUser(testUser2Id, testUser2Name),
    )

    private fun initTestUser(id: String, userName: String): UserPersistenceEntity {
        return UserPersistenceEntity(id, LocalDateTime.now().toString(), LocalDateTime.now().toString(), userName, "Test1")
    }

    override fun persist(persistableObject: UserPersistenceEntity): UserPersistenceEntity {
        userMap[persistableObject.id] = persistableObject
        return persistableObject
    }

    override fun delete(id: Id): Boolean {
        userMap.remove(id.toString()) ?: return false
        return true
    }

    override fun exists(userName: String): Boolean {
        return userMap.values.firstOrNull { u -> u.userName == userName } != null
    }

    override fun exists(id: Id): Boolean {
        return userMap.containsKey(id.toString())
    }

    override fun getBy(userName: String): UserPersistenceEntity? {
        return userMap.values.firstOrNull { u -> u.userName == userName }
    }

    override fun getBy(id: Id): UserPersistenceEntity? {
        return userMap[id.toString()]
    }

    override fun getAll(): List<UserPersistenceEntity> {
        return userMap.values.stream().toList()
    }
}
