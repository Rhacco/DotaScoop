package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import github.com.rhacco.dota2androidapp.sources.remote.sDota2OfficialAPIService
import io.reactivex.Single

object TopLiveGamesRemoteDataSource : TopLiveGamesDataSource {
    override fun getTopLiveGames(): Single<List<TopLiveGameEntity>> =
            Single.create(
                    { subscriber ->
                        sDota2OfficialAPIService
                                .fetchTopLiveGames(App.instance.getString(R.string.api_key), 0)
                                .map { it }
                                .subscribe(
                                        { result ->
                                            subscriber.onSuccess(result.topLiveGamesList())
                                        },
                                        { _ ->
                                            subscriber.onError(Exception())
                                        })
                    }
            )
}