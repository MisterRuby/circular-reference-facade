package ruby.resolvingcircularreferences.contract.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.entity.Campaign
import ruby.resolvingcircularreferences.campaign.service.CampaignService
import ruby.resolvingcircularreferences.contract.entity.Contract
import ruby.resolvingcircularreferences.contract.entity.ContractHistory
import ruby.resolvingcircularreferences.contract.repository.ContractHistoryRepository
import ruby.resolvingcircularreferences.contract.repository.ContractRepository
import ruby.resolvingcircularreferences.contract.request.ContractPatch
import ruby.resolvingcircularreferences.contract.request.ContractPost
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class ContractService{

    @Autowired
    private lateinit var contractRepository: ContractRepository
    @Autowired
    private lateinit var contractHistoryRepository: ContractHistoryRepository
    @Autowired
    private lateinit var campaignService: CampaignService

    @Transactional
    fun post(contractPost: ContractPost, campaign: Campaign) {
        val contract = contractPost.run {
            Contract(
                name = name,
                startDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                endDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                amount = amount,
                campaign = campaign,
                campaignVersion = campaign.version,
            )
        }
        contractRepository.save(contract)

        postHistory(contract)
    }

    @Transactional
    fun postHistory(contract: Contract) {
        val contractHistory = ContractHistory(
            contract = contract,
            version = contract.version,
            name = contract.name,
            startDate = contract.startDate,
            endDate = contract.endDate,
            amount = contract.amount,
            campaignVersion = contract.campaignVersion,
        )

        contractHistoryRepository.save(contractHistory)
    }

    @Transactional
    fun patch(id: Long, contractPatch: ContractPatch) {
        val contract = contractRepository.findByIdOrNull(id) ?: throw RuntimeException("contract not found")
        val contracts = contractRepository.findByCampaign(contract.campaign)

        val campaign = contract.campaign
        campaign.increaseVersion()

        contract.run {
            name = contractPatch.name
            startDate = contractPatch.startDate.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            endDate = contractPatch.endDate.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            amount = contractPatch.amount
            campaignVersion = contract.campaign.version
        }

        contracts.forEach {
            it.increaseVersion()
            it.campaignVersion = it.campaign.version
        }

        contracts.forEach { postHistory(it) }

        campaign.run {
            startDate = contracts.minOf { it.startDate }
            endDate = contracts.maxOf { it.endDate }
            amount = contracts.sumOf { it.amount }
        }
        campaignService.postHistory(campaign)
    }
}
