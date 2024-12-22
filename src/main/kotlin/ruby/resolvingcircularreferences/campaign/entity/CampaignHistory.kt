package ruby.resolvingcircularreferences.campaign.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class CampaignHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val version: Int = 0,
    val name: String, // 캠페인 이름
    val startDate: LocalDate, // 시작일
    val endDate: LocalDate, // 종료일
    val amount: BigDecimal, // 계약 금액

    @CreationTimestamp
    val historyRegisteredAt: LocalDateTime? = null, // 이력 등록일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    val campaign: Campaign, // 참조되는 광고 캠페인
)
