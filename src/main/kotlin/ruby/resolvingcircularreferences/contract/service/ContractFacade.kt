package ruby.resolvingcircularreferences.contract.service

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.service.facade.CampaignHistoryService
import ruby.resolvingcircularreferences.contract.repository.ContractRepository
import ruby.resolvingcircularreferences.contract.request.ContractPatch
import ruby.resolvingcircularreferences.contract.service.facade.ContractHistoryService
import ruby.resolvingcircularreferences.contract.service.facade.ContractPatchService

@Component
class ContractFacade(
    private val contractRepository: ContractRepository,
    private val campaignHistoryService: CampaignHistoryService,
    private val contractPatchService: ContractPatchService,
    private val contractHistoryService: ContractHistoryService
){
    @Transactional
    fun patch(id: Long, contractPatch: ContractPatch) {
        val contract = contractPatchService.patch(id, contractPatch)

        val contracts = contractRepository.findByCampaign(contract.campaign)
        contracts.forEach {
            it.increaseVersion()
            it.campaignVersion = it.campaign.version
        }

        contracts.forEach { contractHistoryService.postHistory(it) }

        val campaign = contract.campaign
        campaign.run {
            startDate = contracts.minOf { it.startDate }
            endDate = contracts.maxOf { it.endDate }
            amount = contracts.sumOf { it.amount }
        }
        campaignHistoryService.postHistory(campaign)
    }
}
