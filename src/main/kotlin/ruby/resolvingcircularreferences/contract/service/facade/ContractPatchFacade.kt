package ruby.resolvingcircularreferences.contract.service.facade

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.contract.entity.Contract
import ruby.resolvingcircularreferences.contract.repository.ContractRepository
import ruby.resolvingcircularreferences.contract.request.ContractPatch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class ContractPatchFacade(
    private val contractRepository: ContractRepository
){
    @Transactional
    fun patch(id: Long, contractPatch: ContractPatch): Contract {
        val contract = contractRepository.findByIdOrNull(id) ?: throw RuntimeException("contract not found")
        val campaign = contract.campaign
        campaign.increaseVersion()

        return contract.apply {
            name = contractPatch.name
            startDate = contractPatch.startDate.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            endDate = contractPatch.endDate.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            amount = contractPatch.amount
            campaignVersion = contract.campaign.version
        }
    }
}
