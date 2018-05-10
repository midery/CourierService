package com.liarstudio.courierservice.ui.screen.main.packages.controller

import android.view.ViewGroup
import android.widget.TextView
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.coordinates.Coordinates
import com.liarstudio.courierservice.entitiy.pack.Package
import ru.surfstudio.easyadapter.recycler.controller.BindableItemController
import ru.surfstudio.easyadapter.recycler.holder.BindableViewHolder

class PackageListElementController(
        private val onPackageClick: (Long) -> Unit,
        private val onShowOnMapClick: (Coordinates) -> Unit
) : BindableItemController<Package, PackageListElementController.Holder>() {

    override fun getItemId(pack: Package) = pack.id

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Package>(parent, R.layout.list_packages_item) {

        val nameTv = itemView.findViewById<TextView>(R.id.name_tv)
        val dateTv = itemView.findViewById<TextView>(R.id.date_tv)
        val statusTv = itemView.findViewById<TextView>(R.id.status_tv)
        val showOnMapBtn = itemView.findViewById<TextView>(R.id.show_on_map_btn)


        override fun bind(pack: Package) {
            val res = itemView.resources
            nameTv.text = pack.name
            dateTv.text = res.getString(R.string.pack_item_date_tv, pack.stringDate)
            statusTv.text = res.getString(R.string.pack_item_status_tv, pack.status)

            itemView.setOnClickListener { onPackageClick(pack.id) }
            showOnMapBtn.setOnClickListener { onShowOnMapClick(Coordinates(pack.coordinates[0], pack.coordinates[1])) }
        }
    }

}