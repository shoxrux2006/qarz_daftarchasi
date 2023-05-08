package uz.gita.qarzdaftarchasi.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.gita.qarzdaftarchasi.utils.Const


@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey()
    val id: Long,
    var name: String,
    var phoneNumber: String,
    var images: List<String>,
    var sum: Long,
    var paidSum: Long,
    var deadLine: Long
) : java.io.Serializable

fun DocumentSnapshot.toDebtEntity(): DebtEntity {
    val images: List<String> = Gson().fromJson(
        this.getString(Const.images) ?: "[]",
        object : TypeToken<List<String?>?>() {}.type
    )
    return DebtEntity(
        this.id.toLong(),
        this.getString(Const.name) ?: "",
        this.getString(Const.phone) ?: "",
        images,
        this.getLong(Const.sum) ?: 0,
        this.getLong(Const.paidSum) ?: 0,
        this.getLong(Const.deadline) ?: 0
    )

}

