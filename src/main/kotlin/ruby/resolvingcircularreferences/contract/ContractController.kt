package ruby.resolvingcircularreferences.contract

import org.springframework.web.bind.annotation.*
import ruby.resolvingcircularreferences.contract.request.ContractPatch
import ruby.resolvingcircularreferences.contract.service.ContractFacade

@RestController
@RequestMapping("/contracts")
class ContractController(private val contractFacade: ContractFacade) {

    @PatchMapping("/{id}")
    fun patch(@PathVariable id: Long, @RequestBody contractPatch: ContractPatch) {
        contractFacade.patch(id, contractPatch)
    }
}
