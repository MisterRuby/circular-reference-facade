package ruby.resolvingcircularreferences.campaign.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.repository.CampaignRepository
import ruby.resolvingcircularreferences.campaign.request.CampaignPatch
import ruby.resolvingcircularreferences.campaign.request.CampaignPost
import ruby.resolvingcircularreferences.campaign.service.facade.CampaignHistoryFacade
import ruby.resolvingcircularreferences.campaign.service.facade.CampaignPostFacade
import ruby.resolvingcircularreferences.contract.repository.ContractRepository
import ruby.resolvingcircularreferences.contract.service.facade.ContractHistoryFacade
import ruby.resolvingcircularreferences.contract.service.facade.ContractPostFacade

@Service
class CampaignService(
    private val campaignRepository: CampaignRepository,
    private val contractRepository: ContractRepository,
    private val campaignPostFacade: CampaignPostFacade,
    private val campaignHistoryFacade: CampaignHistoryFacade,
    private val contractPostFacade: ContractPostFacade,
    private val contractHistoryFacade: ContractHistoryFacade
){
    @Transactional
    fun post(campaignPost: CampaignPost) {
        val contracts = campaignPost.contracts
        val campaign = campaignPostFacade.post(campaignPost)

        campaignHistoryFacade.postHistory(campaign)

        contracts.forEach {
            val contract = contractPostFacade.post(it, campaign)
            contractHistoryFacade.postHistory(contract)
        }
    }


    @Transactional
    fun patch(id: Long, campaignPatch: CampaignPatch) {
        val campaign = campaignRepository.findByIdOrNull(id) ?: throw RuntimeException("campaign not found")
        campaign.let {
            it.name = campaignPatch.name
            it.increaseVersion()
        }

        campaignHistoryFacade.postHistory(campaign)

        val contracts = contractRepository.findByCampaign(campaign)
        contracts.forEach {
            it.increaseVersion()
            it.campaignVersion = it.campaign.version
        }

        contracts.forEach { contractHistoryFacade.postHistory(it) }
    }
}
