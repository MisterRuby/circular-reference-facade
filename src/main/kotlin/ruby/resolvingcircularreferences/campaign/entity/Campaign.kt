package ruby.resolvingcircularreferences.campaign.entity

import jakarta.persistence.*
import ruby.resolvingcircularreferences.contract.entity.Contract
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class Campaign(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var version: Int = 0, // PK: Version 값
    var name: String, // 캠페인 이름
    var startDate: LocalDate, // 시작일
    var endDate: LocalDate, // 종료일
    var amount: BigDecimal, // 계약 금액

    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val histories: List<CampaignHistory> = mutableListOf(), // 캠페인 이력 목록

    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val contracts: List<Contract> = mutableListOf() // 캠페인 이력 목록
) {
    fun increaseVersion() {
        this.version += 1
    }
}
