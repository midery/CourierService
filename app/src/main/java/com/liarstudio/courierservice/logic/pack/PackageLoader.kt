package com.liarstudio.courierservice.logic.pack

import com.liarstudio.courierservice.entitiy.pack.Package
import javax.inject.Inject

class PackageLoader @Inject constructor(
        private val packageApi: PackageApi
){
    fun getPackages() = packageApi.getPackages()
    fun getPackage(id: Long) = packageApi.getPackage(id)
    fun getCourierPackages(id: Long) = packageApi.getCourierPackages(id)
    fun getNewCourierPackages(id: Long) = packageApi.getNewCourierPackages(id)
    fun getAdminPackages() = packageApi.getAdminPackages()
    fun delete(id: Long) = packageApi.delete(id)
    fun add(p: Package) = packageApi.add(p)
}