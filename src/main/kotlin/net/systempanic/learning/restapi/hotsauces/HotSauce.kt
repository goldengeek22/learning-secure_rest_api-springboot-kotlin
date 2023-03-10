package net.systempanic.learning.restapi.hotsauces

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob

/**
 * @author Alexandre AMEVOR
 */

@Entity
data class HotSauce(

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    var brandName: String = "",
    var sauceName: String = "",
    @Lob
    var description: String = "",
    @Lob
    var url: String = "",
    var heat: Int = 0
)