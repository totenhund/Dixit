package totenhund.com.dixit.database

import android.content.Context
import androidx.room.*

@Database(entities = [Card::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        fun getInstance(context: Context): CardDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        CardDatabase::class.java,
                        "card_database"
                ).build()


    }

}