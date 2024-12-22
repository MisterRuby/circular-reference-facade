package ruby.resolvingcircularreferences.campaign.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ruby.resolvingcircularreferences.campaign.entity.Campaign

@Repository
interface CampaignRepository : JpaRepository<Campaign, Long>
