package eu.karcags.spacekmp.cache

import eu.karcags.spacekmp.Image
import eu.karcags.spacekmp.LaunchStatus
import eu.karcags.spacekmp.RocketLaunch

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun getAllLaunchesInfo(): List<RocketLaunch> {
        return dbQuery.selectAllLaunchesInfo(::mapLaunchSelecting).executeAsList()
    }

    internal fun clearAndCreateLaunches(launches: List<RocketLaunch>) {
        dbQuery.transaction {
            dbQuery.removeAllLaunches()
            launches.forEach { launch ->
                dbQuery.insertLaunch(
                    flightNumber = launch.id,
                    missionName = launch.missionName,
                    launchDateUTC = launch.launchDateUTC,
                    imageSmall = launch.image.small,
                    imageLarge = launch.image.large,
                    statusId = launch.status.id.toLong(),
                    statusName = launch.status.name,
                    statusDescription = launch.status.description,
                )
            }
        }
    }

    private fun mapLaunchSelecting(
        flightNumber: String,
        missionName: String,
        launchDateUTC: String,
        imageSmall: String,
        imageLarge: String,
        statusId: Long,
        statusName: String,
        statusDescription: String,
    ): RocketLaunch {
        return RocketLaunch(
            id = flightNumber,
            missionName = missionName,
            launchDateUTC = launchDateUTC,
            image = Image(imageSmall, imageLarge),
            status = LaunchStatus(statusId.toInt(), statusName, statusDescription),
        )
    }
}