package ruby.resolvingcircularreferences.contract.service.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.campaign.entity.Campaign
import ruby.resolvingcircularreferences.contract.entity.Contract
import ruby.resolvingcircularreferences.contract.repository.ContractRepository
import ruby.resolvingcircularreferences.contract.request.ContractPost
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class ContractPostService(
    private val contractRepository: ContractRepository
){
    @Transactional
    fun post(contractPost: ContractPost, campaign: Campaign): Contract {
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
        return contractRepository.save(contract)
    }
}
