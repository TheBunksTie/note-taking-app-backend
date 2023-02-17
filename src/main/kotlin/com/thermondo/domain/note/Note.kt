package com.thermondo.domain.note

import com.thermondo.domain.common.DomainException
import com.thermondo.domain.model.ChangedAt
import com.thermondo.domain.model.CreatedAt
import com.thermondo.domain.model.DomainEntity
import com.thermondo.domain.model.Id
import com.thermondo.domain.user.User

/**
 * Domain entity representing a note with main properties [title], [body], [tags], [author], [noteType]
 * @param title the title of the note
 * @param body the body of the note
 * @param tags the list of tags of the note
 * @param author the authoring and owning [User] of a note
 * @param noteType the type of the note
 */
class Note(
    id: Id,
    createdAt: CreatedAt,
    changedAt: ChangedAt,
    var title: Title,
    var body: Body?,
    var tags: Set<Tag>,
    var author: User,
    var noteType: NoteType
) : DomainEntity(id, createdAt, changedAt) {

    companion object FactoryMethods {

        fun fromAuthor(author: User): NoteBuilder {
            return NoteBuilder().withAuthor(author)
        }

        fun new(): NoteBuilder {
            return NoteBuilder()
        }
    }

    class NoteBuilder {

        private var id: Id? = Id.generate()
        private var createdAt: CreatedAt? = CreatedAt.now()
        private var changedAt: ChangedAt? = ChangedAt.now()

        private var title: Title? = null
        private var body: Body? = null
        private var tags: Set<Tag>? = HashSet()
        private var author: User? = null
        private var type: NoteType? = NoteType.PRIVATE

        fun withId(id: Id): NoteBuilder {
            this.id = id
            return this
        }

        fun withCreatedAt(createdAt: CreatedAt): NoteBuilder {
            this.createdAt = createdAt
            return this
        }

        fun withChangedAt(changedAt: ChangedAt): NoteBuilder {
            this.changedAt = changedAt
            return this
        }

        fun withTitle(title: Title): NoteBuilder {
            this.title = title
            return this
        }

        fun withBody(body: Body): NoteBuilder {
            this.body = body
            return this
        }

        fun withTags(tags: Set<Tag>): NoteBuilder {
            this.tags = tags.toSet()
            return this
        }

        fun withAuthor(author: User): NoteBuilder {
            this.author = author
            return this
        }

        fun withNoteType(type: NoteType): NoteBuilder {
            this.type = type
            return this
        }

        fun create(): Note {
            if (title == null)
                throw NoTitleException()

            if (author == null)
                throw NoAuthorException()

            return Note(id!!, createdAt!!, changedAt!!, title!!, body, tags!!, author!!, type!!)
        }

        class NoTitleException : DomainException("A note must have a title")

        class NoAuthorException : DomainException("A note must have an authoring user")
    }
}
