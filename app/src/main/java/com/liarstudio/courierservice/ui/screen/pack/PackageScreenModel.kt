package com.liarstudio.courierservice.ui.screen.pack

import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.entitiy.pack.PackStatus
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.ui.base.screen.model.BaseScreenModel

/**
 * Модель экрана создания/редактирования посылки [PackageActivity]
 *
 */
class PackageScreenModel : BaseScreenModel() {

    var packId: Long = 0
    var pack: Pack? = null
    var couriers = emptyList<User>()

    //Флаг, показывающий, открыта ли посылка в режиме только для чтения
    val isReadOnly: Boolean get() = pack?.status == PackStatus.DELIVERED
    var isForEdit = false
    //Флаг, показывающий, является ли текущий пользователь администратором
    var isAdmin = false
    //Флаг, показывающий необходима ли модели полная перерисовка
    var needsFullRender = false

    //Названия всех статусов
    var allStatusNames = arrayOf("Новая", "Назначенная", "В процессе", "Отклоненная", "Завершенная")
    //Названия для статусов, которые может выбрать пользователь
    var statusNames = emptyList<String>()

    /**
     * Функция обновления [statusNames] на основе статуса посылки и параметра [isAdmin]
     */
    fun reloadStatusNames() {
        statusNames = if (isAdmin) {
            when (pack!!.status) {
                PackStatus.NEW -> listOf("Новая", "Назначенная", "Завершенная")
                PackStatus.SET -> listOf("Назначенная", "Завершенная")
                PackStatus.IN_PROCESS -> listOf("В процессе", "Назначенная", "Завершенная")
                PackStatus.REJECTED -> listOf("Отклоненная", "Назначенная", "Завершенная")
                PackStatus.DELIVERED -> listOf("Завершенная")
            }
        } else {
            when (pack!!.status) {
                PackStatus.NEW -> listOf("Новая")
                PackStatus.SET -> listOf("Назначенная", "В процессе", "Отклоненная")
                PackStatus.IN_PROCESS -> listOf("В процессе", "Отклоненная", "Завершенная")
                PackStatus.REJECTED -> listOf("Отклоненная", "Назначенная", "Завершенная")
                PackStatus.DELIVERED -> listOf("Завершенная")
            }
        }
    }
}