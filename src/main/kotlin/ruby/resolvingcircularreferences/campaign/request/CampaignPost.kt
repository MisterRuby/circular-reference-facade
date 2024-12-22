package ruby.resolvingcircularreferences.campaign.request

import ruby.resolvingcircularreferences.contract.request.ContractPost


data class CampaignPost(
    val name: String, // 캠페인 이름
    val contracts: List<ContractPost> = mutableListOf(),
)
