package ruby.resolvingcircularreferences.contract.service.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ruby.resolvingcircularreferences.contract.entity.Contract
import ruby.resolvingcircularreferences.contract.entity.ContractHistory
import ruby.resolvingcircularreferences.contract.repository.ContractHistoryRepository

@Service
class ContractHistoryFacade(
    private val contractHistoryRepository: ContractHistoryRepository
){
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
}
