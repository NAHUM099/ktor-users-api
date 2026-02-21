package com.ktor.repository.interfaces

import com.ktor.models.Category

interface ICategoryRepository {
    fun create (name : String, description : String?) : Int
    fun findAll () : List<Category>
    fun findById (id : Int) : Category?
    fun findByName (name : String) : Category?
    fun update (id: Int, name : String? , description : String?) : Boolean
    fun delete (id : Int) : Boolean
}