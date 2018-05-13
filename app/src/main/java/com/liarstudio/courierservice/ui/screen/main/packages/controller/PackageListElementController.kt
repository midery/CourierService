package com.liarstudio.courierservice.ui.screen.main.packages.controller

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.entitiy.person.Coordinates
import ru.surfstudio.easyadapter.recycler.controller.BindableItemController
import ru.surfstudio.easyadapter.recycler.holder.BindableViewHolder

class PackageListElementController(
        private val onPackageClick: (Long) -> Unit,
        private val onShowOnMapClick: (Coordinates) -> Unit
) : BindableItemController<Pack, PackageListElementController.Holder>() {

    override fun getItemId(pack: Pack) = pack.hashCode().toLong()

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Pack>(parent, R.layout.list_packages_item) {

        val nameTv = itemView.findViewById<TextView>(R.id.name_tv)
        val dateTv = itemView.findViewById<TextView>(R.id.date_tv)
        val statusTv = itemView.findViewById<TextView>(R.id.status_tv)
        val showOnMapBtn = itemView.findViewById<ImageView>(R.id.show_on_map_btn)


        override fun bind(pack: Pack) {
            val res = itemView.resources
            nameTv.text = pack.name
            dateTv.text = res.getString(R.string.pack_item_date_tv, pack.date)
            statusTv.text = res.getString(R.string.pack_item_status_tv, pack.status.pos)

            itemView.setOnClickListener { onPackageClick(pack.id) }
            showOnMapBtn.setOnClickListener { onShowOnMapClick(pack.coordinates) }
        }
    }

}