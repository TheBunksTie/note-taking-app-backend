package com.thermondo.persistence.note

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.NoteType
import com.thermondo.persistence.note.abstraction.INotePersistenceRepository
import com.thermondo.persistence.user.UserPersistenceEntity
import com.thermondo.persistence.user.UserPersistenceRepository
import java.time.LocalDateTime
import java.util.UUID

/**
 * In memory repository to store [NotePersistenceEntity]
 * Not to be used for production use, only for demonstration purposes
 */
class NotePersistenceRepository : INotePersistenceRepository {

    // TODO obviously in production there would be a mechanism to load test data from a file
    //  or populate a DB via script and no hardcoded values here
    private val noteMap: MutableMap<String, NotePersistenceEntity> = initTestNoteRepository()

    private fun initTestNoteRepository(): MutableMap<String, NotePersistenceEntity> {
        val noteList = listOf(
            initTestNote(
                UserPersistenceRepository.testUser1Name, "body11", setOf("tag111", "tag112"),
                UserPersistenceRepository.testUser1Id, NoteType.PUBLIC.toString()
            ),
            initTestNote(
                UserPersistenceRepository.testUser1Name, "body12", setOf("tag121"),
                UserPersistenceRepository.testUser1Id, NoteType.PRIVATE.toString()
            ),
            initTestNote(
                UserPersistenceRepository.testUser2Name, "body21", emptySet(),
                UserPersistenceRepository.testUser2Id, NoteType.PUBLIC.toString()
            ),
            initTestNote(
                UserPersistenceRepository.testUser2Name, "body22", setOf("tag221", "tag222", "tag223"),
                UserPersistenceRepository.testUser2Id, NoteType.PRIVATE.toString()
            ),
        )

        return noteList.associateBy(keySelector = { n -> n.id }, valueTransform = { n -> n }).toMutableMap()
    }

    private fun initTestNote(
        authorName: String,
        body: String,
        tags: Set<String>,
        authorId: String,
        noteType: String
    ): NotePersistenceEntity {
        return NotePersistenceEntity(
            UUID.randomUUID().toString(),
            LocalDateTime.now().toString(),
            LocalDateTime.now().toString(),
            "title of note",
            body,
            tags,
            initTestUser(authorId, authorName),
            noteType
        )
    }

    private fun initTestUser(id: String, userName: String): UserPersistenceEntity {
        return UserPersistenceEntity(id, LocalDateTime.now().toString(), LocalDateTime.now().toString(), userName, "Test1")
    }

    override fun persist(persistableObject: NotePersistenceEntity): NotePersistenceEntity {
        noteMap[persistableObject.id] = persistableObject
        return persistableObject
    }

    override fun delete(id: Id): Boolean {
        noteMap.remove(id.toString()) ?: return false
        return true
    }

    override fun exists(id: Id): Boolean {
        return noteMap.containsKey(id.toString())
    }

    override fun getBy(id: Id): NotePersistenceEntity? {
        return noteMap[id.toString()]
    }

    override fun getAll(): List<NotePersistenceEntity> {
        return noteMap.values.stream().toList().sortedBy { n -> n.createdAt }
    }

    override fun getAllByAuthor(authorId: String): List<NotePersistenceEntity> {
        return getAll().filter { n -> isAuthoredBy(n, authorId) }
    }

    override fun getAllByTags(authorId: String, tags: Set<String>): List<NotePersistenceEntity> {
        return getAll().filter { n -> isAuthoredBy(n, authorId) && n.tags.intersect(tags).isNotEmpty() }
    }

    override fun getAllByKeywords(authorId: String, keywords: Set<String>): List<NotePersistenceEntity> {
        return getAll().filter { n -> isAuthoredBy(n, authorId) && containsAnyKeyword(n, keywords) }
    }

    override fun getAllPublic(): List<NotePersistenceEntity> {
        return getAll().filter { n -> n.noteType == NoteType.PUBLIC.toString() }
    }

    private fun isAuthoredBy(note: NotePersistenceEntity, authorId: String): Boolean {
        return note.author.id == authorId
    }

    private fun containsAnyKeyword(note: NotePersistenceEntity, keywords: Set<String>): Boolean {
        return containsKeyword(note.body, keywords)
    }

    private fun containsKeyword(searchBody: String, keywords: Set<String>): Boolean {
        return keywords.any { k -> searchBody.contains(k) }
    }
}
