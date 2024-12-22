package ruby.resolvingcircularreferences.contract.entity

import jakarta.persistence.*
import ruby.resolvingcircularreferences.campaign.entity.Campaign
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class Contract (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var version: Int = 0, // PK: Version 값
    var name: String, // 청약 이름
    var startDate: LocalDate, // 시작일
    var endDate: LocalDate, // 종료일
    var amount: BigDecimal, // 계약 금액
    var campaignVersion: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id",)
    var campaign: Campaign,

    @OneToMany(mappedBy = "contract", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val histories: List<ContractHistory> = mutableListOf(), // 광고 청약 목록
) {
    fun increaseVersion() {
        this.version += 1
    }
}
