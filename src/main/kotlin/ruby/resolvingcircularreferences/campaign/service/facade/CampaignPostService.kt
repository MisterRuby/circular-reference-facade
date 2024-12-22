package ruby.resolvingcircularreferences.campaign.service.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.entity.Campaign
import ruby.resolvingcircularreferences.campaign.repository.CampaignRepository
import ruby.resolvingcircularreferences.campaign.request.CampaignPost
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CampaignPostService(
    private val campaignRepository: CampaignRepository
){
    @Transactional
    fun post(campaignPost: CampaignPost): Campaign {
        val contracts = campaignPost.contracts
        val campaign = Campaign(
            name = campaignPost.name,
            startDate = contracts.minOf { it.startDate }
                .let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) },
            endDate = contracts.maxOf { it.endDate }
                .let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) },
            amount = contracts.sumOf { it.amount }
        )
        return campaignRepository.save(campaign)
    }
}
