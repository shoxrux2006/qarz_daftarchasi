package uz.gita.qarzdaftarchasi.domain.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(debtEntity: DebtEntity)

    @Update
    suspend fun update(debtEntity: DebtEntity)

    @Delete
    suspend fun delete(debtEntity: DebtEntity)


    @Delete
    suspend fun delete(debtEntity: List<DebtEntity>)

    @Query("SELECT *FROM debts ORDER by name")
    fun getBooks(): Flow<List<DebtEntity>>


    @Transaction
    suspend fun updateOfflineDebt(list: List<DebtEntity>) {
        delete()
        insert(list)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(debtEntity: List<DebtEntity>)

    @Query("DELETE FROM debts")
    suspend fun delete()

    @Query("SELECT*FROM debts WHERE name LIKE :str")
    fun search(str: String): Flow<List<DebtEntity>>
}