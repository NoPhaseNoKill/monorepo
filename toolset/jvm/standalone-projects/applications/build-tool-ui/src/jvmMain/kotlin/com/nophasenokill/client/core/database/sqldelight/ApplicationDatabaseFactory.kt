package com.nophasenokill.client.core.database.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.nophasenokill.client.core.database.sqldelight.generated.ApplicationDatabase

class ApplicationDatabaseFactory {
    fun createDatabase(driver: SqlDriver): ApplicationDatabase {
        driver.migrateTo(ApplicationDatabase.Schema)
        return ApplicationDatabase(driver)
    }
}
