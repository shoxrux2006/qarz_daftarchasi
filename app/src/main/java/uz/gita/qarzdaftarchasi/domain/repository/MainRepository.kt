package uz.gita.qarzdaftarchasi.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.utils.NetworkResponse


interface MainRepository {
    fun search(string: String): Flow<List<DebtEntity>>
    fun getOnlineDebt(): Flow<List<DebtEntity>>
    suspend fun setOfflineDebt(debtEntity: List<DebtEntity>)
    fun updateDebt(debtEntity: DebtEntity): Flow<NetworkResponse<DebtEntity>>
    fun addDebt(debtEntity: DebtEntity): Flow<NetworkResponse<Any>>
    suspend fun deleteDebt(debtEntity: List<DebtEntity>)
    fun deleteDebt(debtEntity: DebtEntity): Flow<NetworkResponse<DebtEntity>>
   suspend fun addDebtOffline(debtEntity: DebtEntity)
   suspend fun updateDebtOffline(debtEntity: DebtEntity)
   suspend fun deleteDebtOffline(debtEntity: DebtEntity)
    fun getDebts(): Flow<List<DebtEntity>>
}