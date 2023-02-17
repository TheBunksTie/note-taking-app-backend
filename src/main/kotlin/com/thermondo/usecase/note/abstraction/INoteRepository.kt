package com.thermondo.usecase.note.abstraction

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.Keyword
import com.thermondo.domain.note.Note
import com.thermondo.domain.note.Tag
import com.thermondo.usecase.abstraction.IDomainRepository

/**
 * Extension interface to default repository to add more actions for accessing
 * the [Note] data
 */
interface INoteRepository : IDomainRepository<Note> {
    // TODO maybe a more generic way to retrieve sub-sets of notes is possible like
    //  Spring Boot custom queries, https://stackoverflow.com/a/42967126
    fun getAllByAuthor(authorId: Id): List<Note>

    fun getAllByTags(authorId: Id, tags: Set<Tag>): List<Note>

    fun getAllByKeywords(authorId: Id, keywords: Set<Keyword>): List<Note>

    fun getAllPublic(): List<Note>
}
