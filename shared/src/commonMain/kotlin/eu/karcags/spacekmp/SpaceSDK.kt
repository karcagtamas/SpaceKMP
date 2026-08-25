package eu.karcags.spacekmp

import eu.karcags.spacekmp.cache.Database
import eu.karcags.spacekmp.cache.DatabaseDriverFactory
import eu.karcags.spacekmp.network.SpaceApi

class SpaceSDK(databaseDriverFactory: DatabaseDriverFactory, val api: SpaceApi) {
    private val database = Database(databaseDriverFactory)

    @Throws(Exception::class)
    suspend fun getLaunches(forceLoad: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunchesInfo()

        return if (cachedLaunches.isNotEmpty() && !forceLoad) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearAndCreateLaunches(it)
            }
        }
    }
}