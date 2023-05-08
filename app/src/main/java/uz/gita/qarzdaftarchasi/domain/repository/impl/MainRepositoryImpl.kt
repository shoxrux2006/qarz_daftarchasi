package uz.gita.qarzdaftarchasi.domain.repository.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.qarzdaftarchasi.domain.repository.MainRepository
import uz.gita.qarzdaftarchasi.domain.room.DebtDAO
import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.domain.room.toDebtEntity
import uz.gita.qarzdaftarchasi.utils.Const
import uz.gita.qarzdaftarchasi.utils.NetworkResponse
import java.io.File
import javax.inject.Inject

class MainRepositoryImpl
@Inject constructor(

    private val debtDAO: DebtDAO
) : MainRepository {

    private lateinit var uploadTask: UploadTask
    val baseRoot: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun search(string: String): Flow<List<DebtEntity>> =
        debtDAO.search("${string}%")

    override fun getOnlineDebt(): Flow<List<DebtEntity>> = callbackFlow<List<DebtEntity>> {
        baseRoot.collection(Const.debt).addSnapshotListener { value, error ->
            value?.let { its ->
                val data = its.documents.map { it.toDebtEntity() }
                trySend(data)
            }
        }
        baseRoot.collection(Const.debt).get()
            .addOnSuccessListener { its ->
                val data = its.documents.map { it.toDebtEntity() }

                trySend(data)
            }.addOnFailureListener {

            }
        awaitClose {
            channel.close()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun setOfflineDebt(debtEntity: List<DebtEntity>) {
        debtDAO.updateOfflineDebt(debtEntity)
    }

    override fun updateDebt(debtEntity: DebtEntity): Flow<NetworkResponse<DebtEntity>> =
        callbackFlow<NetworkResponse<DebtEntity>> {
            val map: MutableMap<String, Any> = HashMap()
            map[Const.name] = debtEntity.name
            map[Const.phone] = debtEntity.phoneNumber
            map[Const.images] = Gson().toJson(debtEntity.images)
            map[Const.sum] = debtEntity.sum
            map[Const.paidSum] = debtEntity.paidSum
            map[Const.deadline] = debtEntity.deadLine
            val base = baseRoot.collection(Const.debt).document(debtEntity.id.toString())
            base.addSnapshotListener { value, error ->
                value?.let {
                    trySend(NetworkResponse.Success(it.toDebtEntity()))
                }
            }
            base.update(map).addOnSuccessListener {
                trySend(NetworkResponse.Success(debtEntity))
            }
                .addOnFailureListener {
                    trySend(NetworkResponse.Failure())
                }
            awaitClose {
                channel.close()
            }
        }.flowOn(Dispatchers.IO)

    override fun addDebt(debtEntity: DebtEntity): Flow<NetworkResponse<Any>> =
        callbackFlow<NetworkResponse<Any>> {

            val storage = FirebaseStorage.getInstance()
            debtEntity.images.forEach {
                val ref = storage.reference.child(it)
                uploadTask = ref.putFile(Uri.fromFile(File(it)))
                uploadTask.addOnSuccessListener {

                }.addOnFailureListener {
                }
            }

            val map: MutableMap<String, Any> = HashMap()
            map[Const.name] = debtEntity.name
            map[Const.phone] = debtEntity.phoneNumber
            map[Const.images] = Gson().toJson(debtEntity.images)
            map[Const.sum] = debtEntity.sum
            map[Const.paidSum] = debtEntity.paidSum
            map[Const.deadline] = debtEntity.deadLine

            baseRoot.collection(Const.debt).document(System.currentTimeMillis().toString()).set(map)
                .addOnSuccessListener {
                    trySend(NetworkResponse.Success(Any()))
                }.addOnFailureListener {
                    trySend(NetworkResponse.Failure())
                }


            awaitClose {
                channel.close()
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun deleteDebt(debtEntity: List<DebtEntity>) {
        debtEntity.forEach {
            val base = baseRoot.collection(Const.debt).document(it.id.toString())
            base.delete()
        }
        debtDAO.delete(debtEntity)
    }

    override fun deleteDebt(debtEntity: DebtEntity): Flow<NetworkResponse<DebtEntity>> =
        callbackFlow<NetworkResponse<DebtEntity>> {
            val base = baseRoot.collection(Const.debt).document(debtEntity.id.toString())
            base.delete().addOnFailureListener {
                trySend(NetworkResponse.Failure())
            }
            awaitClose {
                channel.close()
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun addDebtOffline(debtEntity: DebtEntity) {
        debtDAO.insert(debtEntity)
    }

    override suspend fun updateDebtOffline(debtEntity: DebtEntity) {
        debtDAO.update(debtEntity)
    }

    override suspend fun deleteDebtOffline(debtEntity: DebtEntity) {
        debtDAO.delete(debtEntity)
    }

    override fun getDebts(): Flow<List<DebtEntity>> =
        debtDAO.getBooks()
}