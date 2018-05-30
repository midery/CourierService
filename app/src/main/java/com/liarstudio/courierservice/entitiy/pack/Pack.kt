package com.liarstudio.courierservice.entitiy.pack


import com.liarstudio.courierservice.entitiy.person.Coordinates
import com.liarstudio.courierservice.entitiy.person.Person
import com.liarstudio.courierservice.ui.base.EMPTY_STRING
import com.orm.SugarRecord
import com.orm.dsl.Ignore
import com.orm.dsl.NotNull

class Pack(
        var status: PackStatus,
        var courierId: Int,
        var sender: Person,
        var recipient: Person,
        var name: String,
        var dimensions: Dimensions,
        var date: String,
        var commentary: String,
        price: Double
) : SugarRecord() {

    var price = price
        private set

    var coordinates: Coordinates
        get() = recipient.coordinates
        set(coordinates) {
            recipient.coordinates = coordinates
        }

    constructor() : this(
            PackStatus.NEW,
            0,
            Person(),
            Person(),
            EMPTY_STRING,
            Dimensions(.0, .0, 0.0, 0.0),
            EMPTY_STRING,
            EMPTY_STRING,
            0.0
    )

    companion object {

        @Ignore
        private val sizeCoefficient = 0.3937
        @Ignore
        private val weightCoefficient = 2.2
        @Ignore
        private val tariff = 200.0

        //http://www.bagagesdumonde.com/en/lost-and-found/faq/79
        @Ignore
        private val cargoRate = 250.0


        /*
    ****** STATIC AND HELP METHODS AREA ******
    */

        @Ignore
        var SIZE_PROGRAM_STATE = 1

        @Ignore
        var WEIGHT_PROGRAM_STATE = 1
    }
}
