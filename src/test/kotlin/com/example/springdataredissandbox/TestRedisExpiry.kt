package com.example.springdataredissandbox

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.UUID
import java.util.concurrent.TimeUnit

val redis: GenericContainer<*> =
    GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
        .withExposedPorts(6379)
        .also { it.start() }

@Testcontainers
@SpringBootTest
class TestRedisExpiry {

    @Autowired
    lateinit var dataRepo: DataRepo

    @Test
    internal fun `expiry should work`() {
        dataRepo.save(Data(name = "Amir", users = listOf(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()
        )))
        Assertions.assertThat(dataRepo.count())
            .isEqualTo(1)
        val single: Data = dataRepo.findAll().single()
        Assertions.assertThat(single.users)
            .hasSize(3)
        TimeUnit.SECONDS.sleep(8)
        Assertions.assertThat(dataRepo.findAll())
            .isEmpty()
    }

    companion object {
//        @Container
//        @JvmStatic
//        val redis: GenericContainer<*> =
//            GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
//                .withExposedPorts(6379)

        @DynamicPropertySource
        @JvmStatic
        fun prepareSpring(registry: DynamicPropertyRegistry) {
            registry.add("spring.redis.url") {
                "redis://localhost:${redis.getMappedPort(6379)}"
                    .also { println("it = ${it}") }
            }
        }
    }
}
