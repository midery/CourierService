package com.liarstudio.courierservice.logic.pack.response

import com.google.gson.annotations.SerializedName
import com.liarstudio.courierservice.entitiy.pack.Coordinates
import com.liarstudio.courierservice.entitiy.pack.Dimensions
import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.entitiy.pack.PackStatus
import com.liarstudio.courierservice.entitiy.person.Person
import com.liarstudio.courierservice.entitiy.person.PersonType
import com.liarstudio.courierservice.logic.base.EntityHolder

/**
 * Маппинг-модель посылки
 */
class PackResponse(
        val id: Long,
        val status: Int,
        val courierId: Long,
        val sender: PersonResponse,
        val recipient: PersonResponse,
        val name: String,
        val dimensions: DimensionsResponse,
        val coordinates: CoordinatesResponse,
        val date: String,
        val commentary: String,
        val price: Double

) : EntityHolder<Pack> {

    override fun toEntity() = Pack(
            PackStatus.getByPos(status),
            courierId,
            sender.toEntity(),
            recipient.toEntity(),
            name,
            dimensions.toEntity(),
            coordinates.toEntity(),
            date,
            commentary,
            price).apply { id = this@PackResponse.id }
}

/**
 * Маппинг-модель клиента
 */
class PersonResponse(@SerializedName("id") val id: Long,
                     @SerializedName("type") val type: Int,
                     @SerializedName("address") var address: String,
                     @SerializedName("name") var name: String,
                     @SerializedName("email") var email: String,
                     @SerializedName("phone") var phone: String,
                     @SerializedName("companyName") var companyName: String
) : EntityHolder<Person> {

    override fun toEntity() = Person(
            PersonType.getByPos(type),
            address,
            name,
            email,
            phone,
            companyName
    ).apply { id = this@PersonResponse.id }
}

/**
 * Маппинг-модель размеров посылки
 */
class DimensionsResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("x") val x: Double,
        @SerializedName("y") val y: Double,
        @SerializedName("z") val z: Double,
        @SerializedName("weight") val weight: Double
) : EntityHolder<Dimensions> {
    override fun toEntity() = Dimensions(id, x, y, z, weight)
}

/**
 * Маппинг-модель координат
 */
class CoordinatesResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("latitude") val x: Double,
        @SerializedName("longitude") val y: Double
) : EntityHolder<Coordinates> {
    override fun toEntity() = Coordinates(id, x, y)
}