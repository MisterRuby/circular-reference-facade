package ruby.resolvingcircularreferences.campaign.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.repository.CampaignRepository
import ruby.resolvingcircularreferences.campaign.request.CampaignPatch
import ruby.resolvingcircularreferences.campaign.request.CampaignPost
import ruby.resolvingcircularreferences.campaign.service.facade.CampaignHistoryService
import ruby.resolvingcircularreferences.campaign.service.facade.CampaignPostService
import ruby.resolvingcircularreferences.contract.repository.ContractRepository
import ruby.resolvingcircularreferences.contract.service.facade.ContractHistoryService
import ruby.resolvingcircularreferences.contract.service.facade.ContractPostService

@Component
class CampaignFacade(
    private val campaignRepository: CampaignRepository,
    private val contractRepository: ContractRepository,
    private val campaignPostService: CampaignPostService,
    private val campaignHistoryService: CampaignHistoryService,
    private val contractPostService: ContractPostService,
    private val contractHistoryService: ContractHistoryService
){
    @Transactional
    fun post(campaignPost: CampaignPost) {
        val contracts = campaignPost.contracts
        val campaign = campaignPostService.post(campaignPost)

        campaignHistoryService.postHistory(campaign)

        contracts.forEach {
            val contract = contractPostService.post(it, campaign)
            contractHistoryService.postHistory(contract)
        }
    }


    @Transactional
    fun patch(id: Long, campaignPatch: CampaignPatch) {
        val campaign = campaignRepository.findByIdOrNull(id) ?: throw RuntimeException("campaign not found")
        campaign.let {
            it.name = campaignPatch.name
            it.increaseVersion()
        }

        campaignHistoryService.postHistory(campaign)

        val contracts = contractRepository.findByCampaign(campaign)
        contracts.forEach {
            it.increaseVersion()
            it.campaignVersion = it.campaign.version
        }

        contracts.forEach { contractHistoryService.postHistory(it) }
    }
}
