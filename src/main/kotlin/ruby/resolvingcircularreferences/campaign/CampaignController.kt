package ruby.resolvingcircularreferences.campaign

import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ruby.resolvingcircularreferences.campaign.request.CampaignPatch
import ruby.resolvingcircularreferences.campaign.request.CampaignPost
import ruby.resolvingcircularreferences.campaign.service.CampaignFacade

@RestController
@RequestMapping("/campaigns")
class CampaignController(private val campaignFacade: CampaignFacade) {

    @PostMapping
    fun post(@RequestBody campaignPost: CampaignPost) {
        campaignFacade.post(campaignPost)
    }

    @PatchMapping("/{id}")
    fun patch(@PathVariable id: Long, @RequestBody campaignPatch: CampaignPatch) {
        campaignFacade.patch(id, campaignPatch)
    }
}
