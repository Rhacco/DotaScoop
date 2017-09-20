package github.com.raccok.dota2androidapp.sources.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.com.raccok.dota2androidapp.entities.HeroEntity
import io.reactivex.Flowable

@Dao
interface HerosDao {

    @Query("SELECT * FROM heros")
    fun loadAllHeros(): Flowable<List<HeroEntity>>

    @Query("SELECT * FROM heros WHERE localized_name = :heroLocalName LIMIT 1")
    fun getHeroByLocalName(heroLocalName: String): Flowable<List<HeroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: MutableList<HeroEntity>): Unit

}