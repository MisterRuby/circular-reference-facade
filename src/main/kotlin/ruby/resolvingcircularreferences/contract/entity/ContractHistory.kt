package ruby.resolvingcircularreferences.contract.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class ContractHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val version: Int = 0, // PK: Version 값
    val name: String, // 청약 이름
    val startDate: LocalDate, // 시작일
    val endDate: LocalDate, // 종료일
    val amount: BigDecimal, // 계약 금액
    val campaignVersion: Int,

    @CreationTimestamp
    val historyRegisteredAt: LocalDateTime? = null, // 이력 등록일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    val contract: Contract, // 참조되는 청약

)
