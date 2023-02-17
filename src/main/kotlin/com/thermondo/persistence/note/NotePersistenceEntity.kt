package com.thermondo.persistence.note

import com.thermondo.persistence.common.PersistenceEntityBase
import com.thermondo.persistence.user.UserPersistenceEntity

/**
 * Persistence entity representing a note with main properties [title], [body], [tags], [author], [noteType]
 * mirroring the actual domain entity note
 * @param title the title of the note
 * @param body the body of the note
 * @param tags the list of tags of the note
 * @param author the authoring and owning [UserPersistenceEntity] of a note
 * @param noteType the type of the note
 */
class NotePersistenceEntity(
    id: String,
    createdAt: String,
    changedAt: String,
    val title: String,
    val body: String,
    val tags: Set<String>,
    val author: UserPersistenceEntity,
    val noteType: String
) :
    PersistenceEntityBase(id, createdAt, changedAt)
// TODO id and createdAt / changedAt / noteType  could be more fitting data types in an actual DB
