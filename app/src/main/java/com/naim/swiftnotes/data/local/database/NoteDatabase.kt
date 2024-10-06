package com.naim.swiftnotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.naim.swiftnotes.core.constant.DatabaseConst
import com.naim.swiftnotes.data.local.dao.NoteDao
import com.naim.swiftnotes.domain.model.Note

@Database(
    entities = [Note::class],
    version = DatabaseConst.NOTES_DATABASE_VERSION,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}