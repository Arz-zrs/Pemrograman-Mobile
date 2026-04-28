package com.example.scrollablemodul3.model

import com.example.scrollablemodul3.R

class DataSource {
    fun loadItems(): List<ScrollableData> {
        return listOf(
            ScrollableData(1, R.string.item1, R.string.item1_sub, R.string.item1_desc, R.string.item1_detail, R.drawable.crosscode, R.drawable.crosscode_detail, "https://store.steampowered.com/app/368340/CrossCode/"),
            ScrollableData(2, R.string.item2, R.string.item2_sub, R.string.item2_desc, R.string.item2_detail, R.drawable.hades2, R.drawable.hades2_detail, "https://store.steampowered.com/app/1145350/Hades_II/"),
            ScrollableData(3, R.string.item3, R.string.item3_sub, R.string.item3_desc, R.string.item3_detail, R.drawable.nms, R.drawable.nms_detail, "https://store.steampowered.com/app/275850/No_Mans_Sky/"),
            ScrollableData(4, R.string.item4, R.string.item4_sub, R.string.item4_desc, R.string.item4_detail, R.drawable.coe33, R.drawable.coe33_detail, "https://store.steampowered.com/app/1903340/Clair_Obscur_Expedition_33/"),
            ScrollableData(5, R.string.item5, R.string.item5_sub, R.string.item5_desc, R.string.item5_detail, R.drawable.sekiro, R.drawable.sekiro_detail, "https://store.steampowered.com/app/814380/Sekiro_Shadows_Die_Twice__GOTY_Edition/")
        )
    }
}