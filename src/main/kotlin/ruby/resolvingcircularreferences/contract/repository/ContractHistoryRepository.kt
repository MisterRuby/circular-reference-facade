package ruby.resolvingcircularreferences.contract.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ruby.resolvingcircularreferences.contract.entity.ContractHistory

@Repository
interface ContractHistoryRepository : JpaRepository<ContractHistory, Long>
