package com.liarstudio.courierservice.entitiy.pack


import com.liarstudio.courierservice.entitiy.person.Person
import com.liarstudio.courierservice.ui.base.EMPTY_STRING
import com.liarstudio.courierservice.ui.utils.DateFormatter
import java.util.*
import com.orm.SugarRecord
import com.orm.dsl.Ignore

open class Pack(
        var status: PackStatus,
        var courierId: Long,
        var sender: Person,
        var recipient: Person,
        var name: String,
        var dimensions: Dimensions,
        var coordinates: Coordinates,
        var date: String,
        var commentary: String,
        price: Double
) : SugarRecord() {

    var price = price
        private set

    constructor() : this(
            PackStatus.NEW,
            0,
            Person(),
            Person(),
            EMPTY_STRING,
            Dimensions(0L, .0, .0, 0.0, 0.0),
            Coordinates(0L, .0, .0),
            EMPTY_STRING,
            EMPTY_STRING,
            0.0
    )

    fun calculatePrice(): Double {
        val v = dimensions.x * dimensions.y * dimensions.z / 1000000.0
        val g = dimensions.weight / v


        var fastShippingMultiplier = 1;

        //Доставка за 2 дня
        val today = DateFormatter.today()
        val dateValue = DateFormatter.parseCalendar(date)
        if (today.get(Calendar.YEAR) == dateValue.get(Calendar.YEAR)
                && (dateValue.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) < 2))
            fastShippingMultiplier *= 2

        if (g > cargoRate)
            price = dimensions.weight * tariff * fastShippingMultiplier
        else
            price = v * cargoRate * tariff * fastShippingMultiplier
        return price
    }


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
