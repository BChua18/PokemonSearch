package com.example.chua.pokemonsearch

import ir.mirrajabi.searchdialog.core.Searchable

/**
 * Created by Chua on 3/19/2018.
 */
class SearchModel(private var mTitle: String?, private var mNumber: Int?): Searchable
{
    override fun getTitle(): String {
        return mTitle!!
    }

    fun getNumber(): Int {
        return mNumber!!
    }
}