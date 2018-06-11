package com.liarstudio.courierservice.logic.pack.request

import com.google.gson.annotations.SerializedName
import com.liarstudio.courierservice.entitiy.pack.Coordinates
import com.liarstudio.courierservice.entitiy.pack.Dimensions
import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.entitiy.person.Person

/**
 * Маппинг-модель посылки
 */
class PackageRequest(
        @SerializedName("id") val id: Long,
        @SerializedName("status") val status: Int,
        @SerializedName("courierId") val courierId: Long,
        @SerializedName("sender") val sender: PersonRequest,
        @SerializedName("recipient") val recipient: PersonRequest,
        @SerializedName("name") val name: String,
        @SerializedName("dimensions") val dimensions: DimensionsRequest,
        @SerializedName("coordinates") val coordinates: CoordinatesRequest,
        @SerializedName("date") val date: String,
        @SerializedName("commentary") val commentary: String,
        @SerializedName("price") val price: Double

) {
    constructor(pack: Pack) : this(
            pack.id,
            pack.status.pos,
            pack.courierId,
            PersonRequest(pack.sender),
            PersonRequest(pack.recipient),
            pack.name,
            DimensionsRequest(pack.dimensions),
            CoordinatesRequest(pack.coordinates),
            pack.date,
            pack.commentary,
            pack.price
    )
}


/**
 * Маппинг-модель клиента
 */
class PersonRequest(@SerializedName("id") val id: Long,
                    @SerializedName("type") val type: Int,
                    @SerializedName("address") var address: String,
                    @SerializedName("name") var name: String,
                    @SerializedName("email") var email: String,
                    @SerializedName("phone") var phone: String,
                    @SerializedName("companyName") var companyName: String
) {
    constructor(person: Person) : this(
            person.id,
            person.type.pos,
            person.address,
            person.name,
            person.email,
            person.phone,
            person.companyName
    )
}


/**
 * Маппинг-модель размеров посылки
 */
class DimensionsRequest(@SerializedName("id") val id: Long,
                        @SerializedName("x") val x: Double,
                        @SerializedName("y") val y: Double,
                        @SerializedName("z") val z: Double,
                        @SerializedName("weight") val weight: Double
) {
    constructor(dimensions: Dimensions) : this(
            dimensions.id,
            dimensions.x,
            dimensions.y,
            dimensions.z,
            dimensions.weight)

}

/**
 * Маппинг-модель координат
 */
class CoordinatesRequest(@SerializedName("id") val id: Long,
                         @SerializedName("latitude") val latitude: Double,
                         @SerializedName("longitude") val longitude: Double
) {
    constructor(coordinates: Coordinates) : this(
            coordinates.id,
            coordinates.latitude,
            coordinates.longitude
    )
}