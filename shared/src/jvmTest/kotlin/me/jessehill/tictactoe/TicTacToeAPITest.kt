package me.jessehill.tictactoe

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import me.jessehill.application.api.apiRoutes
import me.jessehill.application.plugins.configureMonitoring
import me.jessehill.application.plugins.configureResources
import me.jessehill.application.plugins.configureSerialization
import me.jessehill.database.DatabaseDriverFactory
import me.jessehill.database.configureDatabases
import me.jessehill.serializers.CommonSerializerModule
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class TicTacToeAPITest {
    private var databaseDriverFactory = DatabaseDriverFactory()

    @Before
    fun before() {
        println(System.getenv("USE_MEMORY_DB"))
        // Reset the database so that we have a fresh start for each test
        databaseDriverFactory = DatabaseDriverFactory()
    }

    // We need to override the default SDK, so we control an in-memory database.
    @Test
    fun testRoot() = testApplication {
        setup(databaseDriverFactory)

        val response = customClient.get("/api/users")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, world!", response.bodyAsText())
    }

    companion object {
        private val ROOT_PATH = "/api"

        private fun ApplicationTestBuilder.setup(databaseDriverFactory: DatabaseDriverFactory) {
            application {
                configureDatabases(databaseDriverFactory)
                configureSerialization()
                configureMonitoring()
                configureResources()
                apiRoutes()
            }
        }

        private val ApplicationTestBuilder.customClient
            get() = createClient {
                install(ContentNegotiation) {
                    json(CommonSerializerModule.json)
                }
            }
    }
}