package com.liarstudio.courierservice.ui.screen.pack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.widget.itemSelections
import com.jakewharton.rxbinding2.widget.textChanges
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.entitiy.pack.PackStatus
import com.liarstudio.courierservice.entitiy.person.Person
import com.liarstudio.courierservice.entitiy.person.PersonType
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.ui.base.screen.LoadState
import com.liarstudio.courierservice.ui.base.isVisible
import com.liarstudio.courierservice.ui.base.positionChanges
import com.liarstudio.courierservice.ui.base.screen.view.BaseActivity
import kotlinx.android.synthetic.main.activity_package_fields.*
import javax.inject.Inject

/**
 * Экран создания/редактирования посылки
 */
class PackageActivity : BaseActivity<PackageScreenModel>() {

    @Inject
    lateinit var presenter: PackagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    override fun requestPresenter() = presenter

    override fun getLayout() = R.layout.activity_package_fields

    override fun renderState(loadState: LoadState) {
        package_pb.isVisible = loadState == LoadState.LOADING
    }

    override fun renderData(screenModel: PackageScreenModel) {

        screenModel.pack?.let {//Показ данных происходит только когда pack != null

            //Если модель необходимо полностью перерисовать - перерисовываем
            //все составные части и инвертируем значение параметра
            if (screenModel.needsFullRender) {
                renderSender(it.sender)
                renderRecipient(it.recipient)
                renderPack(it)
                renderPackStatus(screenModel)
                if (screenModel.couriers.isNotEmpty()) {
                    courier_list_spinner.isVisible = true
                    renderCouriers(screenModel.couriers, it.courierId)
                }
                screenModel.needsFullRender = false
            }

            if (screenModel.isReadOnly)
                renderReadonly()

            //Скрываем поля ввода компании, если человек - частное лицо
            sender_company_name_et.isVisible = it.sender.type == PersonType.COMPANY
            recipient_company_et.isVisible = it.recipient.type == PersonType.COMPANY
            //Показываем поле ввода комментария только если статус - Завершенная или Отклоненная
            commentary_et.isVisible = it.status == PackStatus.DELIVERED ||
                    it.status == PackStatus.REJECTED
            date_tv.text = it.date
            package_price_tv.text = it.price.toString()
            delete_btn.isVisible = screenModel.isAdmin
        }
    }

    /**
     * Событие, срабатываемое при закрытии Activity, запущенной на этом экране
     *
     * @param requestCode код запроса
     * @param resultCode код ответа
     * @param data данные
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        presenter.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Показ ошибки валидации адреса отпраавителя
     */
    fun showSenderAddressError() { sender_address_et.error = getString(R.string.validation_error) }

    /**
     * Показ ошибки валидации имени отправителя
     */
    fun showSenderNameError() { sender_name_et.error = getString(R.string.validation_error)  }

    fun showSenderEmailError() { sender_email_et.error = getString(R.string.validation_error)  }

    fun showSenderPhoneError() { sender_phone_et.error = getString(R.string.validation_error)  }

    fun showRecipientAddressError() { recipient_address_et.error = getString(R.string.validation_error) }

    fun showRecipientNameError() { recipient_name_et.error = getString(R.string.validation_error)  }

    fun showRecipientEmailError() { recipient_email_et.error = getString(R.string.validation_error)  }

    fun showRecipientPhoneError() { recipient_phone_et.error = getString(R.string.validation_error)  }

    fun showPackageNameError() { package_name_et.error = getString(R.string.validation_error)  }

    fun showPackageCommentError() { commentary_et.error = getString(R.string.validation_error)  }

    /**
     * Отрисовка отправителя
     *
     * @param sender отправитель
     */
    private fun renderSender(sender: Person) {
        sender_address_et.setText(sender.address)
        sender_name_et.setText(sender.name)
        sender_email_et.setText(sender.email)
        sender_phone_et.setText(sender.phone)
        sender_company_name_et.setText(sender.companyName)
        sender_type_spinner.setSelection(sender.type.ordinal)
        sender_company_name_et.setText(sender.companyName)
    }

    /**
     * Отрисовка получателя
     *
     * @param recipient получатель
     */
    private fun renderRecipient(recipient: Person) {
        recipient_address_et.setText(recipient.address)
        recipient_name_et.setText(recipient.name)
        recipient_email_et.setText(recipient.email)
        recipient_phone_et.setText(recipient.phone)
        recipient_company_et.setText(recipient.companyName)
        recipient_type_spinner.setSelection(recipient.type.ordinal)
        recipient_company_et.setText(recipient.companyName)
    }

    /**
     * Отрисовка списка курьеров
     *
     * @param couriers список курьеров
     * @param currentId id текущего курьера
     */
    private fun renderCouriers(couriers: List<User>, currentId: Long) {
        val names = mutableListOf<String>()
        couriers.forEach { names.add("${it.id}. ${it.name}") }
        val spinnerAdapter = ArrayAdapter(this@PackageActivity,
                R.layout.support_simple_spinner_dropdown_item, names)
        courier_list_spinner.adapter = spinnerAdapter
        val currentPosition = couriers.indexOfFirst { it.id == currentId }
        courier_list_spinner.setSelection(currentPosition)
        status_spinner.itemSelections() //observe
    }

    /**
     * Отрисовка посылки
     *
     * @param pack посылка
     */
    private fun renderPack(pack: Pack) {
        date_tv.text = pack.date

        package_name_et.setText(pack.name)
        val dimens = pack.dimensions

        package_size_w_et.setText(dimens.x.toString())
        package_size_h_et.setText(dimens.y.toString())
        package_size_d_et.setText(dimens.z.toString())
        package_weight_et.setText(dimens.weight.toString())
        status_tv.isVisible = true
        status_spinner.isVisible = true

        if (pack.commentary.isNotEmpty()) {
            commentary_et.setText(pack.commentary)
            commentary_et.isVisible = true
        }
    }

    /**
     * Отрисовка статуса посылки
     * Перезагружает список названий статусов на основе текущего, и устанавливает их в spinner
     *
     * @param screenModel модель, содержащая статус и названия статусов
     */
    private fun renderPackStatus(screenModel: PackageScreenModel) {
        val status = screenModel.pack!!.status

        screenModel.reloadStatusNames()

        status_spinner.adapter = ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, screenModel.statusNames)

        confirm_btn.text = when (status) {
            PackStatus.NEW -> "Добавить"
            PackStatus.DELIVERED -> "Закрыть"
            else -> "Обновить"
        }
    }

    /**
     * Отрисовка всех полей в режиме только для чтения
     */
    private fun renderReadonly() {
        sender_address_et.isEnabled = false
        sender_name_et.isEnabled = false
        sender_email_et.isEnabled = false
        sender_phone_et.isEnabled = false
        sender_company_name_et.isEnabled = false
        sender_type_spinner.isEnabled = false
        sender_company_name_et.isEnabled = false

        recipient_address_et.isEnabled = false
        recipient_name_et.isEnabled = false
        recipient_email_et.isEnabled = false
        recipient_phone_et.isEnabled = false
        recipient_company_et.isEnabled = false
        recipient_type_spinner.isEnabled = false
        recipient_company_et.isEnabled = false

        date_tv.isEnabled = false

        package_name_et.isEnabled = false
        package_size_w_et.isEnabled = false
        package_size_h_et.isEnabled = false
        package_size_d_et.isEnabled = false
        package_weight_et.isEnabled = false
        buttonSetCoordinates.isEnabled = false
        date_pick_btn.isEnabled = false
        package_calc_price_btn.isEnabled = false

        status_spinner.isEnabled = false
        status_spinner.onItemSelectedListener = null
        courier_list_spinner.isEnabled = false
        commentary_et.visibility = View.VISIBLE
        commentary_et.isEnabled = false
    }

    /**
     * Инициалазазия view при старте экрана
     */
    private fun initViews() {
        initObservers()
        initListeners()
    }

    /**
     * Инициализация listeners для кнопок
     */
    private fun initListeners() {
        date_pick_btn.setOnClickListener { presenter.showDatePickDialog() }
        confirm_btn.setOnClickListener { presenter.makeConfirmAction() }
        package_calc_price_btn.setOnClickListener {
            presenter.validateDimensAndSetPrice(
                    package_size_w_et.text.toString(),
                    package_size_h_et.text.toString(),
                    package_size_d_et.text.toString(),
                    package_weight_et.text.toString())
        }
        buttonSetCoordinates.setOnClickListener { presenter.openMapScreen() }
        delete_btn.setOnClickListener { presenter.showDeletePackageDialog() }
    }

    /**
     * Инициализация observers
     */
    private fun initObservers() {
        initSenderObservers()
        initRecipientObservers()
        initPackageObservers()
    }

    /**
     * Инициализация observers для отправителя
     */
    private fun initSenderObservers() {
        presenter.observeSenderAddressChanges(sender_address_et.textChanges())
        presenter.observeSenderNameChanges(sender_name_et.textChanges())
        presenter.observeSenderEmailChanges(sender_email_et.textChanges())
        presenter.observeSenderPhoneChanges(sender_phone_et.textChanges())
        presenter.observeSenderTypeChanges(sender_type_spinner.positionChanges())
        presenter.observeSenderCompanyChanges(sender_company_name_et.textChanges())
    }

    /**
     * Инициализация observers для получателя
     */
    private fun initRecipientObservers() {
        presenter.observeRecipientAddressChanges(recipient_address_et.textChanges())
        presenter.observeRecipientNameChanges(recipient_name_et.textChanges())
        presenter.observeRecipientEmailChanges(recipient_email_et.textChanges())
        presenter.observeRecipientPhoneChanges(recipient_phone_et.textChanges())
        presenter.observeRecipientCompanyChanges(recipient_company_et.textChanges())
        presenter.observeRecipientTypeChanges(recipient_type_spinner.positionChanges())
    }

    /**
     * Инициализация observers для посылки
     */
    private fun initPackageObservers() {
        presenter.observePackageNameChanges(package_name_et.textChanges())
        presenter.observePackageCommentaryChanges(commentary_et.textChanges())
        presenter.observePackStatusChanges(status_spinner.positionChanges())
    }
}
