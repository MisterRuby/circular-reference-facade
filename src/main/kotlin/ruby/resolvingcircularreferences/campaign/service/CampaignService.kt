package ruby.resolvingcircularreferences.campaign.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.entity.Campaign
import ruby.resolvingcircularreferences.campaign.entity.CampaignHistory
import ruby.resolvingcircularreferences.campaign.repository.CampaignHistoryRepository
import ruby.resolvingcircularreferences.campaign.repository.CampaignRepository
import ruby.resolvingcircularreferences.campaign.request.CampaignPatch
import ruby.resolvingcircularreferences.campaign.request.CampaignPost
import ruby.resolvingcircularreferences.contract.repository.ContractRepository
import ruby.resolvingcircularreferences.contract.service.ContractService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CampaignService{
    @Autowired
    private lateinit var campaignRepository: CampaignRepository
    @Autowired
    private lateinit var campaignHistoryRepository: CampaignHistoryRepository
    @Autowired
    private lateinit var contractRepository: ContractRepository
    @Autowired
    private lateinit var contractService: ContractService

    @Transactional
    fun post(campaignPost: CampaignPost) {
        val contracts = campaignPost.contracts

        val campaign = Campaign(
            name = campaignPost.name,
            startDate = contracts.minOf { it.startDate }
                .let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) },
            endDate = contracts.maxOf { it.endDate }
                .let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) },
            amount = contracts.sumOf { it.amount }
        )
        campaignRepository.save(campaign)

        postHistory(campaign)

        contracts.forEach { contractService.post(it, campaign) }
    }

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

    @Transactional
    fun patch(id: Long, campaignPatch: CampaignPatch) {
        val campaign = campaignRepository.findByIdOrNull(id) ?: throw RuntimeException("campaign not found")
        campaign.run {
            name = campaignPatch.name
            version += 1
        }

        postHistory(campaign)

        val contracts = contractRepository.findByCampaign(campaign)
        contracts.forEach {
            it.version += 1
            it.campaignVersion = campaign.version
        }

        contracts.forEach { contractService.postHistory(it) }
    }
}
