package ruby.resolvingcircularreferences.campaign.service.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.entity.Campaign
import ruby.resolvingcircularreferences.campaign.entity.CampaignHistory
import ruby.resolvingcircularreferences.campaign.repository.CampaignHistoryRepository

@Service
class CampaignHistoryService(
    private val campaignHistoryRepository: CampaignHistoryRepository
){
    @Transactional
    fun postHistory(campaign: Campaign) {
        val campaignHistory = CampaignHistory(
            campaign = campaign,
            version = campaign.version,
            name = campaign.name,
            startDate = campaign.startDate,
            endDate = campaign.endDate,
            amount = campaign.amount
        )
        campaignHistoryRepository.save(campaignHistory)
    }
}
