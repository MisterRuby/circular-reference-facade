package ruby.resolvingcircularreferences.campaign.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ruby.resolvingcircularreferences.campaign.entity.CampaignHistory

@Repository
interface CampaignHistoryRepository : JpaRepository<CampaignHistory, Long>
