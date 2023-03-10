package net.systempanic.learning.restapi.hotsauces

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Optional

/**
 * @author Alexandre AMEVOR
 */

@RequestMapping("/api/hotSauces")
@RestController
class HotSauceController(private val hotSauceRepository: HotSauceRepository) {

    // GET /api/hotSauces
    @GetMapping("")
    fun getAll(
        @RequestParam(value = "brandName", required = false, defaultValue = "") brandNameFilter: String,
        @RequestParam(value = "sauceName", required = false, defaultValue = "") sauceNameFilter: String,
        @RequestParam(value = "description", required = false, defaultValue = "") descriptionFilter: String,
        @RequestParam(value = "minHeat", required = false, defaultValue = "") minHeatFilter: String,
        @RequestParam(value = "maxHeat", required = false, defaultValue = "") maxHeatFilter: String
    ): ResponseEntity<List<HotSauce>> {
        val MAX_SCOVILLE = 3_000_000
        val minHeat = if (minHeatFilter.isNotBlank()) minHeatFilter.toInt() else 0
        val maxHeat = if (maxHeatFilter.isNotBlank()) maxHeatFilter.toInt() else MAX_SCOVILLE

        return ResponseEntity<List<HotSauce>>(hotSauceRepository.findAll()
            .filter { it.brandName.contains(brandNameFilter, true) }
            .filter { it.sauceName.contains(sauceNameFilter, true) }
            .filter { it.description.contains(descriptionFilter, true) }
            .filter { it.heat >= minHeat }
            .filter { it.heat <= maxHeat }, HttpStatus.OK
        )
    }

    // GET api/hotSauces/{id}
    @GetMapping("/{id}")
    fun getHotSauce(@PathVariable id: Long): ResponseEntity<Optional<HotSauce>> {
        return if (hotSauceRepository.existsById(id)) {
            ResponseEntity<Optional<HotSauce>>(hotSauceRepository.findById(id), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // GET api/hotSauces/count
    @GetMapping("/count")
    fun countHotSauces(): ResponseEntity<Long> = ResponseEntity(hotSauceRepository.count(), HttpStatus.OK)

    // POST api/hotSauces
    @PostMapping("")
    fun createHotSauce(@RequestBody hotSauce: HotSauce): ResponseEntity<HotSauce> = ResponseEntity(hotSauceRepository.save(hotSauce), HttpStatus.CREATED)

    // PUT api/hotSauces
    @PutMapping("/{id}")
    fun updateHotSauce(@PathVariable id: Long, @RequestBody hotSauceChanged: HotSauce): ResponseEntity<HotSauce> = if (hotSauceRepository.existsById(id)) {
        val originalSauce = hotSauceRepository.findById(id).get()
        val updatedSauce = HotSauce(
            id = id,
            brandName = hotSauceChanged.brandName.ifBlank { originalSauce.brandName },
            sauceName = hotSauceChanged.sauceName.ifBlank { originalSauce.sauceName },
            description = hotSauceChanged.description.ifBlank { originalSauce.description },
            url = hotSauceChanged.url.ifBlank { originalSauce.url },
            heat = if (hotSauceChanged.heat != 0) hotSauceChanged.heat else originalSauce.heat
        )
        ResponseEntity(hotSauceRepository.save(updatedSauce), HttpStatus.OK)
    } else {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }

    // DELETE api/HotSauces
    @DeleteMapping("/{id}")
    fun deleteHotSauce(@PathVariable id: Long): ResponseEntity<HotSauce?> =
        if (hotSauceRepository.existsById(id)) {
            hotSauceRepository.deleteById(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
}