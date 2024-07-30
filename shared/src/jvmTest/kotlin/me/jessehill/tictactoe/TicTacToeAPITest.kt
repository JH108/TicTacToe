package me.jessehill.tictactoe

import com.benasher44.uuid.uuid4
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import me.jessehill.application.api.apiRoutes
import me.jessehill.application.plugins.configureMonitoring
import me.jessehill.application.plugins.configureResources
import me.jessehill.application.plugins.configureSerialization
import me.jessehill.database.DatabaseDriverFactory
import me.jessehill.database.configureDatabases
import me.jessehill.models.User
import me.jessehill.serializers.CommonSerializerModule
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.http.contentType

class TicTacToeAPITest {
    private var databaseDriverFactory = DatabaseDriverFactory()

    @BeforeEach
    fun before() {
        // Reset the database so that we have a fresh start for each test
        databaseDriverFactory = DatabaseDriverFactory()
    }

    @Test
    fun `given a valid user when registering that user then the app should respond with that user`() = testApplication {
        setup(databaseDriverFactory)

        val response = customClient.post("$ROOT_PATH/register") {
            setBody(NEW_USER)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NEW_USER, CommonSerializerModule.json.decodeFromString(response.body()))
    }

    @Test
    fun `given an invalid user when registering that user then the app should respond with a proper HTTP Status`() = testApplication {
        setup(databaseDriverFactory)

        val invalidUser = """
            {
                "firstName": "Bad",
                "lastName": "Actor"
            }
        """.trimIndent()

        val response = customClient.post("$ROOT_PATH/register") {
            setBody(invalidUser)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotAcceptable, response.status)
        assertEquals("The request body is missing or malformed.", response.body())
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

        private val NEW_USER_ID = uuid4()
        private val NEW_USER = User(
            id = NEW_USER_ID,
            username = "test",
            firstName = "Test",
            lastName = "User"
        )
    }
}