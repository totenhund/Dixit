package totenhund.com.dixit.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_table")
data class Card(
        @PrimaryKey(autoGenerate = true)
        val uid: Int,
        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        val image: Bitmap
)


