package com.liarstudio.courierservice.logic.pack

import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.logic.pack.request.PackageRequest
import javax.inject.Inject

class PackageLoader @Inject constructor(
        private val packageApi: PackageApi
){
    fun getPackages() = packageApi.getPackages()
    fun getPackage(id: Long) = packageApi.getPackage(id)
    fun getUserPackages(id: Long, vararg statuses: Int) = packageApi.getUserPackages(id, *statuses)
    fun getAdminPackages(id: Long) = packageApi.getUserPackages(id, 0, 3, 4)
    fun delete(id: Long) = packageApi.delete(id)
    fun add(p: Pack) = packageApi.add(PackageRequest(p))
}