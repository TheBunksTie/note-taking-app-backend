package com.thermondo.usecase.note.abstraction

import com.thermondo.domain.note.Note
import com.thermondo.usecase.abstraction.IDomainRepository

/**
 * Extension interface to default repository to add more actions for accessing
 * the [Note] data
 */
interface INoteRepository : IDomainRepository<Note>
