package com.liarstudio.courierservice.ui.screen.pack

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.util.Patterns
import com.jakewharton.rxbinding2.InitialValueObservable
import com.liarstudio.courierservice.entitiy.pack.Coordinates
import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.entitiy.pack.PackStatus
import com.liarstudio.courierservice.entitiy.person.PersonType
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.logic.pack.PackageLoader
import com.liarstudio.courierservice.logic.user.UserLoader
import com.liarstudio.courierservice.ui.base.*
import com.liarstudio.courierservice.ui.base.screen.presenter.BasePresenter
import com.liarstudio.courierservice.ui.base.screen.LoadState
import com.liarstudio.courierservice.ui.screen.maps.MapActivity
import com.liarstudio.courierservice.ui.utils.DateFormatter
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

/**
 * Презентер экрана посылки [PackageActivity]
 */
@PerActivity
class PackagePresenter @Inject constructor(
        private val userLoader: UserLoader,
        private val packageLoader: PackageLoader,
        private val messageShower: MessageShower,
        view: PackageActivity
) : BasePresenter<PackageActivity>(view) {

    private val screenModel = PackageScreenModel()

    override fun viewCreated() {
        initScreenModel(view.intent)
    }

    /**
     * Инициализация модели экрана
     *
     * @param data - данные для инициализации
     */
    private fun initScreenModel(data: Intent) {
        val packageAction = data.getSerializableExtra(EXTRA_FIRST) as PackageAction
        screenModel.isForEdit = packageAction == PackageAction.EDIT
        screenModel.isAdmin = userLoader.getCurrentUser().isAdmin
        if (screenModel.isForEdit) {
            screenModel.packId = data.getLongExtra(EXTRA_SECOND, 0)
            loadPackage()
        } else {
            createEmptyPackage()
        }
    }

    /**
     * Событие, срабатываемое при закрытии Activity, запущенной на этом экране
     *
     * @param requestCode код запроса
     * @param resultCode код ответа
     * @param data данные
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_MAP) {
            screenModel.pack!!.coordinates = data.getSerializableExtra("coordinates") as Coordinates
        }
    }

    /**
     * Открытие экрана карты с вводом координат
     */
    fun openMapScreen() {
        screenModel.pack?.let {
            val mapIntent = Intent(view, MapActivity::class.java)
            mapIntent.putExtra("coordinates", it.coordinates)
            view.startActivityForResult(mapIntent, REQUEST_MAP)
        }
    }

    /**
     * Открытие диалога выбора даты
     */
    fun showDatePickDialog() {
        val current = DateFormatter.parseCalendar(screenModel.pack!!.date)
        val dpd = DatePickerDialog(view,
                { _, year: Int, month: Int, day: Int -> onDatePick(year, month, day) },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH))
        dpd.show()
    }

    /**
     * Открытие диалога с подтверждением намерения удалить посылку
     */
    fun showDeletePackageDialog() {
        val builder = AlertDialog.Builder(view)
        builder.setTitle("Уведомление")
                .setMessage("Вы действительно желаете удалить посылку?")
                .setPositiveButton("Продолжить") { _, _ -> deletePackage() }
                .setNegativeButton("Вернуться") { _, _ -> }
        builder.create().show()
    }

    /**
     * Функция валидации посылки и установки цены
     *
     * @param x - строковое значение ширины
     * @param y - строковое значение высоты
     * @param z - строковое значение глубины
     * @param weight - строковое значение веса
     */
    fun validateDimensAndSetPrice(x: String, y: String, z: String, weight: String) {
        screenModel.pack?.let {
            it.dimensions.x = x.toDoubleOrNull() ?: 0.0
            it.dimensions.y = y.toDoubleOrNull() ?: 0.0
            it.dimensions.z = z.toDoubleOrNull() ?: 0.0
            it.dimensions.weight = weight.toDoubleOrNull() ?: 0.0
        }
        if (isDimensValid()) {
            screenModel.pack!!.calculatePrice()
            view.render(screenModel)
        } else {
            showDimensError()
        }
    }

    /**
     * Действие, совершаемое при нажаии на кнопку подтверждения
     */
    fun makeConfirmAction() {

        if (screenModel.isReadOnly) {
            finish()
            return
        }

        val isValid = isEverythingValid()
        screenModel.pack?.let {
            if (isValid) {
                when {
                    it.status == PackStatus.DELIVERED -> showNotifySenderDialog()
                    it.coordinates.isEmpty -> showCoordinatesEmptyDialog()
                    else -> addPackage()
                }
            }
        }
    }

    /**
     * Подписка на событие изменения адреса получателя
     *
     * @param observable Observable c адресом
     */
    fun observeSenderAddressChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.sender.address = it.toString() })
    }

    /**
     * Подписка на событие изменения email получателя
     *
     * @param observable Observable c email
     */
    fun observeSenderEmailChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.sender.email = it.toString() })
    }

    fun observeSenderNameChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.sender.name = it.toString() })
    }

    fun observeSenderPhoneChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.sender.phone = it.toString() })
    }

    fun observeSenderCompanyChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.sender.companyName = it.toString() })
    }

    fun observeRecipientAddressChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.recipient.address = it.toString() })
    }

    fun observeRecipientEmailChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.recipient.email = it.toString() })
    }

    fun observeRecipientNameChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.recipient.name = it.toString() })
    }

    fun observeRecipientPhoneChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.recipient.phone = it.toString() })
    }

    fun observeRecipientCompanyChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.recipient.companyName = it.toString() })
    }

    fun observePackageNameChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable.skipInitialValue(), { screenModel.pack!!.name = it.toString() })
    }

    fun observePackageCommentaryChanges(observable: InitialValueObservable<CharSequence>) {
        subscribe(observable, { screenModel.pack!!.commentary = it.toString() })
    }

    /**
     * Подписка на изменение типа отправителя
     *
     * @param observable Observable позицией типа
     */
    fun observeSenderTypeChanges(observable: Observable<Int>) {
        subscribe(observable, {
            screenModel.pack!!.sender.type = PersonType.getByPos(1 - it)
            view.render(screenModel)
        })
    }

    /**
     * Подписка на изменение типа получателя
     *
     * @param observable Observable с позицией типа
     */
    fun observeRecipientTypeChanges(observable: Observable<Int>) {
        subscribe(observable, {
            screenModel.pack!!.recipient.type = PersonType.getByPos(1 - it)
            view.render(screenModel)
        })
    }

    /**
     * Подписка на изменение типа получателя
     *
     * @param observable Observable с позицией типа
     */
    fun observePackStatusChanges(observable: Observable<Int>) {
        subscribe(observable, {
            with(screenModel) {
                val pos = allStatusNames.indexOf(statusNames[it])
                pack!!.status = PackStatus.getByPos(pos)
                view.render(screenModel)
            }
        })
    }

    /**
     * Добавление посылки и закрытие экрана в случае успеха
     */
    private fun addPackage() {
        screenModel.loadState = LoadState.LOADING
        view.render(screenModel)
        subscribe(packageLoader.add(screenModel.pack!!),
                {
                    finishWithResult(Activity.RESULT_OK)
                })
    }

    /**
     * Удаление посылки и закрытие экрана в случае успеха
     */
    private fun deletePackage() {
        screenModel.loadState = LoadState.LOADING
        view.render(screenModel)
        subscribe(packageLoader.delete(screenModel.packId),
                {
                    finishWithResult()
                })
    }


    /**
     * Загрузка посылки
     */
    private fun loadPackage() {
        screenModel.loadState = LoadState.LOADING
        view.render(screenModel)
        subscribe(packageLoader.getPackage(screenModel.packId),
                {
                    screenModel.needsFullRender = true
                    screenModel.pack = it
                    if (screenModel.isAdmin) {
                        loadCouriers()
                    } else {
                        screenModel.loadState = LoadState.NONE
                        view.render(screenModel)
                    }
                })
    }


    /**
     * Загрузка списка курьеров
     */
    private fun loadCouriers() {
        subscribe(userLoader.loadUsers(), {
            screenModel.needsFullRender = true
            screenModel.couriers = it
            screenModel.loadState = LoadState.NONE
            view.render(screenModel)
        })
    }

    /**
     * Создание пустой посылки
     * Вызывается при добавлении
     */
    private fun createEmptyPackage() {
        val userId = userLoader.getCurrentUser().id
        screenModel.pack = Pack()
        screenModel.pack?.let {
            it.courierId = userId
            it.date = DateFormatter.toString(DateFormatter.tomorrow())
        }
    }

    /**
     * Действие, срабатываемое при выборе даты
     */
    private fun onDatePick(year: Int, month: Int, dayOfMonth: Int) {
        val chosenDate = GregorianCalendar(year, month, dayOfMonth)

        //Если выбранная дата не раньше, чем завтра, обновляем дату у редактируемой посылки
        if (chosenDate > DateFormatter.today()) {
            screenModel.pack?.let { it.date = DateFormatter.toString(chosenDate) }
            view.render(screenModel)
        } else {
            showWrongDateError()
        }
    }

    /**
     * Проверка валидности всех полей экрана
     *
     * @return Boolean
     */
    private fun isEverythingValid() = isSenderValid() && isRecipientValid() && isPackageValid()

    /**
     * Проверка валидности полей посылки
     *
     * @return Boolean
     */
    private fun isPackageValid(): Boolean {
        with(screenModel.pack!!) {
            when {
                !isDimensValid() -> showDimensError()
                name.isBlank() -> view.showPackageNameError()
                screenModel.isAdmin && (status == PackStatus.SET || status == PackStatus.DELIVERED) ->
                    showStatusError()
                status == PackStatus.REJECTED && status == PackStatus.DELIVERED -> view.showPackageCommentError()
                else -> return true
            }
            return false
        }
    }

    /**
     * Проверка валидности полей отправителя
     *
     * @return Boolean
     */
    private fun isSenderValid(): Boolean {
        with(screenModel.pack!!.sender) {
            when {
                address.isBlank() -> view.showSenderAddressError()
                name.isBlank() -> view.showSenderNameError()
                email.isNotBlank() && email.isNotBlank() && !email.matches(Patterns.EMAIL_ADDRESS.toRegex()) ->
                    view.showSenderEmailError()
                phone.length != 10 -> view.showSenderPhoneError()
                else -> return true
            }
            return false
        }
    }

    /**
     * Проверка валидности полей получателя
     *
     * @return Boolean
     */
    private fun isRecipientValid(): Boolean {
        with(screenModel.pack!!.recipient) {
            when {
                address.isBlank() -> view.showRecipientAddressError()
                name.isBlank() -> view.showRecipientNameError()
                email.isNotBlank() && email.isNotBlank() && !email.matches(Patterns.EMAIL_ADDRESS.toRegex()) ->
                    view.showRecipientEmailError()
                phone.length != 10 -> view.showRecipientPhoneError()
                else -> return true
            }
            return false
        }
    }

    /**
     * Проверка валидности размеров и веса посылки
     *
     * @return Boolean
     */
    private fun isDimensValid(): Boolean =
            !screenModel.pack!!.dimensions.isEmpty


    /**
     * Показ ошибки валидации размеров
     */
    private fun showDimensError() {
        messageShower.show("Ошибка! Заданы неверные размеры или вес.")
    }

    /**
     * Показ ошибки статуса
     */
    private fun showStatusError() {
        messageShower.show("Выбран неверный статус!")
    }

    /**
     * Показ ошибки о том, что выбрана слишком ранняя дата
     */
    private fun showWrongDateError() {
        messageShower.show("Невозможно выбрать дату раньше сегодняшней!")
    }


    /**
     * Показ уведомления о том, что координаы пусты
     */
    private fun showCoordinatesEmptyDialog() {
        val builder = AlertDialog.Builder(view)
        builder.setTitle("Уведомление")
                .setMessage("Вы не указали координаты. Желаете продолжить без них?")
                .setPositiveButton("Продолжить") { dialog, which -> addPackage() }
                .setNegativeButton("Вернуться") { dialog, which -> }
        builder.create().show()
    }

    /**
     * Показ диалога с уведомлением отправителя о получении посылки
     */
    private fun showNotifySenderDialog() {
        val builder = AlertDialog.Builder(view)
        builder.setTitle("Уведомление")
                .setMessage("Как вы желаете оповестить заказчика?")
                .setNegativeButton("Телефон") { dialog, which ->
                    val phone = screenModel.pack!!.sender.phone
                    val intentPhone = Intent(Intent.ACTION_DIAL, Uri.parse("tel: $phone"))
                    start(intentPhone)
                    addPackage()
                }
                .setPositiveButton("Не уведомлять") { dialog, which -> addPackage() }
        val email = screenModel.pack?.sender?.email ?: EMPTY_STRING
        if (email.isNotEmpty())
            builder.setNeutralButton("E-mail") { dialog, which ->
                val intentEmail = Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", screenModel.pack!!.sender.email, null))
                start(intentEmail)
                addPackage()
            }
        builder.create().show()
    }

    /**
     * Закрытие текущего экрана
     */
    private fun finish() {
        view.finish()
    }

    /**
     * Закрытие экрана с присвоением результата
     *
     * @param resultCode - код результата
     * По-умолчанию результат положительный
     */
    private fun finishWithResult(resultCode: Int = Activity.RESULT_OK) {
        view.setResult(resultCode)
        finish()
    }

    /**
     * Открытие нового экрана
     */
    private fun start(intent: Intent) {
        view.startActivity(intent)
    }
}
