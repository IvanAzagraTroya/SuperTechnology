package pedidosApi

import com.typesafe.config.ConfigFactory
import java.lang.System.getenv

object Config {
    val config = ConfigFactory.load()
    val connectionString get() = getenv("mongoConnectionString") ?: config.getString("mongo.connectionString")
    val database get() = getenv("mongoDatabase") ?: config.getString("mongo.database")
}