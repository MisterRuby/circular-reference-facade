package ruby.resolvingcircularreferences.contract.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ruby.resolvingcircularreferences.campaign.entity.Campaign
import ruby.resolvingcircularreferences.contract.entity.Contract

@Repository
interface ContractRepository : JpaRepository<Contract, Long> {
    fun findByCampaign(campaign: Campaign): List<Contract> = findAll()
}
