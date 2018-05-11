package com.liarstudio.courierservice.entitiy.person


import com.liarstudio.courierservice.ui.base.EMPTY_STRING
import com.orm.SugarRecord

class Person(var type: PersonType,
             var address: String,
             var name: String,
             var email: String,
             var phone: String,
             var companyName: String,
             var coordinates: Coordinates
) : SugarRecord() {

    constructor(): this(
            PersonType.RECEIVER,
            EMPTY_STRING,
            EMPTY_STRING,
            EMPTY_STRING,
            EMPTY_STRING,
            EMPTY_STRING,
            Coordinates())
}
