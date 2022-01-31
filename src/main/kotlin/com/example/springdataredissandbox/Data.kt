package com.example.springdataredissandbox

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.repository.CrudRepository
import java.util.UUID

@RedisHash("MyData")
class Data(
    @Id
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var users: List<UUID>,
    @TimeToLive
    var ttl: Int = 5
)

interface DataRepo : CrudRepository<Data, String>
