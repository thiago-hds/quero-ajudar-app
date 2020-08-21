package com.br.queroajudar.data.source

import androidx.lifecycle.LiveData
import com.br.queroajudar.categories.CategoriesRemoteDataSource
import com.br.queroajudar.data.Category
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.Constants.CAUSE_TYPE
import com.br.queroajudar.util.Constants.SKILL_TYPE
import com.br.queroajudar.util.resultLiveData
import javax.inject.Inject

class DefaultCategoriesRepository @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource
) {

    fun getCategoriesByType(type:Int): LiveData<ResultWrapper<List<Category>>>? {
        return when(type){
            CAUSE_TYPE  -> getCauses()
            SKILL_TYPE  -> getSkills()
            else        -> null
        }
    }

    fun postCategoriesByType(categoryType: Int, selectedItems: List<Int>?): LiveData<ResultWrapper<Boolean>>? {

        val items: List<Int> = selectedItems ?: listOf()
        return when(categoryType) {
            CAUSE_TYPE  -> postCauses(items)
            SKILL_TYPE  ->postSkills(items)
            else        -> null
        }
    }

    fun getCauses() = resultLiveData(
        networkCall = {remoteDataSource.fetchCauses()}
    )

    fun getSkills() = resultLiveData(
        networkCall = {remoteDataSource.fetchSkills()}
    )

    fun postCauses(causesIds: List<Int>) = resultLiveData(
        networkCall = {remoteDataSource.postUserCauses(causesIds)}
    )

    fun postSkills(skillsIds: List<Int>) = resultLiveData(
        networkCall = {remoteDataSource.postUserSkills(skillsIds)}
    )



}