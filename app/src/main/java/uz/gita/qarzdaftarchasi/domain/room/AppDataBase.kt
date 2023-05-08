package uz.gita.qarzdaftarchasi.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DebtEntity::class],
    version = 1
)
@TypeConverters(Converters::class)

abstract class AppDataBase : RoomDatabase() {

    abstract fun debtDAO(): DebtDAO
}