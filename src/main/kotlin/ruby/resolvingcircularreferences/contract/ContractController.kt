package ruby.resolvingcircularreferences.contract

import org.springframework.web.bind.annotation.*
import ruby.resolvingcircularreferences.contract.request.ContractPatch
import ruby.resolvingcircularreferences.contract.service.ContractService

@RestController
@RequestMapping("/contracts")
class ContractController(private val contractService: ContractService) {

    @PatchMapping("/{id}")
    fun patch(@PathVariable id: Long, @RequestBody contractPatch: ContractPatch) {
        contractService.patch(id, contractPatch)
    }
}
