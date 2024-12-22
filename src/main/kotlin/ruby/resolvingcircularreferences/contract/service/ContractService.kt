package ruby.resolvingcircularreferences.contract.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.service.facade.CampaignHistoryFacade
import ruby.resolvingcircularreferences.contract.repository.ContractRepository
import ruby.resolvingcircularreferences.contract.request.ContractPatch
import ruby.resolvingcircularreferences.contract.service.facade.ContractHistoryFacade
import ruby.resolvingcircularreferences.contract.service.facade.ContractPatchFacade

@Service
class ContractService(
    private val contractRepository: ContractRepository,
    private val campaignHistoryFacade: CampaignHistoryFacade,
    private val contractPatchFacade: ContractPatchFacade,
    private val contractHistoryFacade: ContractHistoryFacade
){
    @Transactional
    fun patch(id: Long, contractPatch: ContractPatch) {
        val contract = contractPatchFacade.patch(id, contractPatch)

        val contracts = contractRepository.findByCampaign(contract.campaign)
        contracts.forEach {
            it.increaseVersion()
            it.campaignVersion = it.campaign.version
        }

        contracts.forEach { contractHistoryFacade.postHistory(it) }

        val campaign = contract.campaign
        campaign.run {
            startDate = contracts.minOf { it.startDate }
            endDate = contracts.maxOf { it.endDate }
            amount = contracts.sumOf { it.amount }
        }
        campaignHistoryFacade.postHistory(campaign)
    }
}
